package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.admin.Admin;
import seedu.address.model.admin.UniqueAdminList;
import seedu.address.model.admin.Username;
import seedu.address.model.job.Job;
import seedu.address.model.job.UniqueJobList;
import seedu.address.model.machine.Machine;
import seedu.address.model.machine.UniqueMachineList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueAdminList admins;
    private final UniqueMachineList machines;
    private final UniqueJobList jobs;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        admins = new UniqueAdminList();
        machines = new UniqueMachineList();
        jobs = new UniqueJobList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the machine list with {@code machines}.
     * {@code machines} must not contain duplicate machines
     */
    public void setMachines(List<Machine> machines) {
        this.machines.setMachines(machines);
    }

    /**
     * Replaces the contents of the job list with {@code jobs}.
     * {@code machines} must not contain duplicate jobs
     */
    public void setJobs(List<Job> jobs) {
        this.jobs.setJobs(jobs);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setMachines(newData.getMachineList());
    }

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a machine that matches the {@code machine}
     */
    public boolean hasMachine(Machine machine) {
        requireNonNull(machine);
        return machines.contains(machine);
    }

    /**
     * Returns true if a machine that matches the {@code machine}
     */
    public boolean hasJob(Job job) {
        requireNonNull(job);
        return jobs.contains(job);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Adds an admin to the address book.
     * The admin must not already exist in the address book.
     */
    public void addAdmin(Admin toAdd) {
        admins.add(toAdd);
    }

    /**
     * Adds a machine if {@code machine} does not exist in the list
     */
    public void addMachine(Machine machine) {
        requireNonNull(machine);
        machines.add(machine);
    }

    public void addAdminWithoutRehash(Admin toAdd) {
        //TODO: Not idiot proof
        admins.add(toAdd);
    }

    /**
     * Adds a machine if {@code machine} does not exist in the list
     */
    public void addJob(Job job) {
        requireNonNull(job);
        jobs.add(job);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void updatePerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * updates an admin in the address book.
     */
    public void updateAdmin(Admin toRemove, Admin toAdd) {
        admins.remove(toRemove);
        admins.add(toAdd);
    }

    public boolean hasAdmin(Admin admin) {
        return admins.contains(admin);
    }

    public Admin findAdmin(Username username) {
        return admins.findAdmin(username);
    }

    public int numAdmins() {
        return admins.size();
    }

    /**
     * updates an admin in the address book.
     */
    public void updateJob(Job toRemove, Job toAdd) {
        jobs.remove(toRemove);
        jobs.add(toAdd);
    }

    //======================== machine methods ================================//

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }


    /**
     * Removes an admin from the address book.
     * The admin must already exist in the address book.
     */
    public void removeAdmin(Admin toRemove) {
        admins.remove(toRemove);
    }

    /**
     * Removes a machine if {@code toRemove} exists in the list
     */
    public void removeMachine(Machine toRemove) {
        requireNonNull(toRemove);
        machines.remove(toRemove);
    }

    /**
     * Removes a job from this AddressBook.
     * The job must exist in the address book.
     */
    public void removeJob(Job key) {
        jobs.remove(key);
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Admin> getAdminList() {
        return admins.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Machine> getMachineList() {
        return machines.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Job> getJobList() {
        return jobs.asUnmodifiableObservableList();
    }

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && persons.equals(((AddressBook) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }

}
