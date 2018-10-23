package seedu.address.model;

import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import seedu.address.model.admin.Admin;
import seedu.address.model.job.Job;
import seedu.address.model.machine.Machine;
import seedu.address.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the admins list.
     * This list will not contain any duplicate admins.
     */
    ObservableList<Admin> getAdminList();


    /**
     * Returns an unmodifiable view of the machines list.
     * This list will not contain any duplicate machines.
     *
     */
    ObservableList<Machine> getMachineList();

    /**
     * Returns an unmodifiable view of jobs list
     * This list will not contain any duplicate jobs
     */
    ObservableList<Job> getJobList();

    /**
     * Returns a unmodifiable view of queue
     * This list should contain the highest priority job in descending order
     */

    ObservableList<Job> getQueueList();

}
