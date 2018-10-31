package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.AdminListChangedEvent;
import seedu.address.commons.events.model.JobListChangedEvent;
import seedu.address.commons.events.model.MachineListChangedEvent;
import seedu.address.model.admin.Admin;
import seedu.address.model.admin.Username;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobName;
import seedu.address.model.job.Status;
import seedu.address.model.job.exceptions.JobNotStartedException;
import seedu.address.model.machine.Machine;
import seedu.address.model.machine.MachineName;
import seedu.address.model.machine.exceptions.MachineNotFoundException;
import seedu.address.model.person.Person;


/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedAddressBook versionedAddressBook;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Admin> filteredAdmins;
    private final FilteredList<Machine> filteredMachines;
    private final FilteredList<Job> filteredJobs;


    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs "
                + userPrefs + "and initial admin");

        versionedAddressBook = new VersionedAddressBook(addressBook);
        filteredPersons = new FilteredList<>(versionedAddressBook.getPersonList());
        filteredMachines = new FilteredList<>(versionedAddressBook.getMachineList());
        filteredAdmins = new FilteredList<>(versionedAddressBook.getAdminList());
        filteredJobs = new FilteredList<>(versionedAddressBook.getJobList());
        //TODO find a better way to change the data according to sorted jobs based on comparator
        indicateJobListChanged();

        // Timer for auto print cleanup
        // credit: https://dzone.com/articles/how-schedule-task-run-interval
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (Job job : versionedAddressBook.getJobList()) {
                    try {
                        if (job.getStatus() == Status.ONGOING && job.isFinished()) {
                            finishJob(job);
                        }
                    } catch (JobNotStartedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Timer timer = new Timer();
        long delay = 60000;
        long intervalPeriod = 60000;
        timer.scheduleAtFixedRate(task, delay, intervalPeriod);

    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        versionedAddressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return versionedAddressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(versionedAddressBook));
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAdminListChanged() {
        raise(new AdminListChangedEvent(versionedAddressBook));
    }

    /** Raises an event to indicate the model has changed */
    private void indicateMachineListChanged() {
        raise(new MachineListChangedEvent(versionedAddressBook));
    }

    /** Raises an event to indicate the model has changed */
    private void indicateJobListChanged() {
        raise(new JobListChangedEvent(versionedAddressBook));
        /**
         * Since when job changes, it implicitly implies that machine list will change too
         */
        raise(new MachineListChangedEvent(versionedAddressBook));
    }

    // ============================== Person methods ======================================= //
    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedAddressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public void addPerson(Person person) {
        versionedAddressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        versionedAddressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    // ============================== Job methods ======================================= //

    @Override
    public boolean hasJob(Job job) {
        requireNonNull(job);
        return versionedAddressBook.hasJob(job);
    }

    @Override
    public void addJob(Job job) throws MachineNotFoundException {
        requireAllNonNull(job);
        //TODO find another way to check if the printer exist before adding job
        //TODO refactor after this is working
        for (Machine m : filteredMachines) {
            if (job.getMachine().getName().fullName.equals(new Machine("AUTO").getName().fullName)) {
                Machine mostFree = getMostFreeMachine();
                job.setMachine(mostFree);
                versionedAddressBook.addJob(job);
                versionedAddressBook.addJobToMachineList(mostFree, job);
                indicateJobListChanged();
                indicateMachineListChanged();
                return;
            }
            if (job.getMachine().getName().fullName.equals(m.getName().fullName)) {
                versionedAddressBook.addJob(job);
                versionedAddressBook.addJobToMachineList(m, job);
                indicateJobListChanged();
                indicateMachineListChanged();
                return;
            }
        }
        throw new MachineNotFoundException();
    }

    @Override
    public void deleteJob(JobName job) {
        requireAllNonNull(job);
        versionedAddressBook.removeJob(job);
        indicateJobListChanged();
        indicateMachineListChanged();
    }

    @Override
    public void updateJob(Job oldJob, Job updatedJob) {
        requireAllNonNull(oldJob, updatedJob);
        versionedAddressBook.updateJob(oldJob, updatedJob);
        indicateJobListChanged();
        indicateMachineListChanged();
    }

    @Override
    public Job findJob(JobName name) {
        requireAllNonNull(name);
        return versionedAddressBook.findJob(name);
    }

    @Override
    public void startJob(JobName name) {
        requireNonNull(name);
        versionedAddressBook.startJob(name);
        indicateJobListChanged();
        indicateMachineListChanged();
    }

    @Override
    public void cancelJob(JobName name) {
        requireAllNonNull();
        versionedAddressBook.cancelJob(name);
        indicateJobListChanged();
        indicateMachineListChanged();

    }

    @Override
    public void restartJob(JobName name) {
        requireAllNonNull();
        versionedAddressBook.restartJob(name);
        indicateJobListChanged();
        indicateMachineListChanged();
    }

    @Override
    public void swapJobs(JobName jobName1, JobName jobName2) {
        versionedAddressBook.swapJobs(jobName1, jobName2);
        versionedAddressBook.commit();
        indicateJobListChanged();


    }

    @Override
    public void finishJob(Job job) {
        versionedAddressBook.finishJob(job);
        indicateJobListChanged();
    }

    @Override
    public void requestDeletion(JobName jobName) {
        versionedAddressBook.requestDeletion(jobName);
        indicateJobListChanged();

    }

    // ============================== Machine methods ======================================= //

    @Override
    public void addMachine(Machine machine) {
        versionedAddressBook.addMachine(machine);
        updateFilteredMachineList(PREDICATE_SHOW_ALL_MACHINES);
        indicateMachineListChanged();
    }

    @Override
    public void removeMachine(Machine toRemove) {
        versionedAddressBook.removeMachine(toRemove);
        indicateMachineListChanged();
    }

    @Override
    public boolean hasMachine(Machine machine) {
        requireNonNull(machine);
        return versionedAddressBook.hasMachine(machine);
    }


    @Override
    public void updateMachine(Machine target, Machine editedMachine) {
        requireAllNonNull(target, editedMachine);
        versionedAddressBook.updateMachine(target, editedMachine);
        indicateMachineListChanged();
    }

    @Override
    public Machine getMostFreeMachine() {
        return versionedAddressBook.getMostFreeMachine();
    }

    @Override
    public Machine findMachine(MachineName machineName) {
        requireNonNull(machineName);
        return versionedAddressBook.findMachine(machineName);
    }

    // ============================== Admin methods ======================================= //

    @Override
    public void addAdmin(Admin admin) {
        versionedAddressBook.addAdmin(admin);
        indicateAdminListChanged();
    }

    //TODO: add tests
    @Override
    public void removeAdmin(Admin admin) {
        versionedAddressBook.removeAdmin(admin);
        indicateAdminListChanged();
    }

    //TODO: add tests
    @Override
    public void updateAdmin(Admin admin, Admin updatedAdmin) {
        versionedAddressBook.addAdmin(updatedAdmin);
        versionedAddressBook.removeAdmin(admin);
        indicateAdminListChanged();
    }

    @Override
    public void setLogin(Admin admin) {
        versionedAddressBook.setLoggedInAdmin(admin);
    }

    @Override
    public void clearLogin() {
        versionedAddressBook.clearLogin();
    }

    @Override
    public boolean isLoggedIn() {
        return versionedAddressBook.isLoggedIn();
    }

    @Override
    public Admin currentlyLoggedIn() {
        return versionedAddressBook.currentlyLoggedIn();
    }

    @Override
    public Admin findAdmin(Username username) {
        requireNonNull(username);
        return versionedAddressBook.findAdmin(username);
    }

    @Override
    public int numAdmins() {
        return versionedAddressBook.numAdmins();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Filtered Machine List Accessors ============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Machine} backed by the internal list of
     * {@code versionedAddressBook}
     */

    @Override
    public ObservableList<Machine> getFilteredMachineList() {
        return FXCollections.unmodifiableObservableList(filteredMachines);
    }

    @Override
    public void updateFilteredMachineList(Predicate<Machine> predicate) {
        requireNonNull(predicate);
        filteredMachines.setPredicate(predicate);
    }


    //=========== Filtered Admins List Accessors ============================================================

    @Override
    public ObservableList<Admin> getFilteredAdminList() {
        return FXCollections.unmodifiableObservableList(filteredAdmins);
    }

    @Override
    public void updateFilteredAdminList(Predicate<Admin> predicate) {
        requireNonNull(predicate);
        filteredAdmins.setPredicate(predicate);
    }

    //=========== Filtered Jobs List Accessors ============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Job} backed by the internal list of
     * {@code versionedAddressBook}
     */

    @Override
    public ObservableList<Job> getFilteredJobList() {
        return FXCollections.unmodifiableObservableList(filteredJobs);
    }

    @Override
    public void updateFilteredJobList(Predicate<Job> predicate) {
        requireNonNull(predicate);
        filteredJobs.setPredicate(predicate);
    }


    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public boolean isUndoLogout() {
        return versionedAddressBook.isUndoLogout();
    }

    @Override
    public boolean isRedoLogin() {
        return versionedAddressBook.isRedoLogin();
    }

    @Override
    public boolean isUndoLogin() {
        return versionedAddressBook.isUndoLogin();
    }

    @Override
    public void undoAddressBook() {
        versionedAddressBook.undo();
        indicateAddressBookChanged();
    }

    @Override
    public void redoAddressBook() {
        versionedAddressBook.redo();
        indicateAddressBookChanged();
    }

    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
    }

    @Override
    public void adminLoginCommitAddressBook() {
        versionedAddressBook.adminLoginCommit();
    }

    @Override
    public void adminLogoutCommitAddressBook() {
        versionedAddressBook.adminLogoutCommit();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedAddressBook.equals(other.versionedAddressBook)
                && (filteredPersons.equals(other.filteredPersons)
                    || filteredMachines.equals(other.filteredMachines));
    }


}
