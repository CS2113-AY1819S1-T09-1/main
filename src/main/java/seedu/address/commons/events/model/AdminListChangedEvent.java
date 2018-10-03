package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;

/** Indicates the AddressBook in the model has changed*/
public class AdminListChangedEvent extends BaseEvent {

    public final ReadOnlyAddressBook data;

    public AdminListChangedEvent(ReadOnlyAddressBook data) {

        // Sending only the relevant data to StorageManager
        AddressBook temp = new AddressBook();
        temp.setAdmins(data.getAdminList());
        this.data = temp;
    }

    @Override
    public String toString() {
        return "number of admins " + data.getAdminList().size();
    }
}
