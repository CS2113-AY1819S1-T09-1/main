package seedu.address.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import seedu.address.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private GuiSettings guiSettings;
    private Path addressBookFilePath;
    private Path makerManagerMachinesFilePath;
    private Path makerManagerAdminsFilePath;
    private Path makerManagerJobsFilePath;

    public UserPrefs() {
        setGuiSettings(500, 500, 0, 0);
        this.addressBookFilePath = Paths.get("data\\addressbook.xml");
        this.makerManagerMachinesFilePath = Paths.get("data\\makerManagerMachines.xml");
        this.makerManagerAdminsFilePath = Paths.get("data\\makerManagerAdmins.xml");
        this.makerManagerJobsFilePath = Paths.get("data\\makerManagerJobs.xml");
    }

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }
    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }
    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
    }

    // ================================ file path methods ================================= //
    public Path getAddressBookFilePath() {
        return addressBookFilePath;
    }
    public void setAddressBookFilePath(Path addressBookFilePath) {
        this.addressBookFilePath = addressBookFilePath;
    }
    public Path getMakerManagerMachinesFilePath() {
        return makerManagerMachinesFilePath;
    }
    public void setMakerManagerMachinesFilePath(Path makerManagerMachinesFilePath) {
        this.makerManagerMachinesFilePath = makerManagerMachinesFilePath;
    }
    public Path getMakerManagerAdminsFilePath() {
        return makerManagerAdminsFilePath;
    }
    public void setMakerManagerAdminsFilePath(Path makerManagerAdminsFilePath) {
        this.makerManagerAdminsFilePath = makerManagerAdminsFilePath;
    }
    public Path getMakerManagerJobsFilePath() {
        return makerManagerJobsFilePath;
    }
    public void setMakerManagerJobsFilePath(Path makerManagerJobsFilePath) {
        this.makerManagerJobsFilePath = makerManagerJobsFilePath;
    }


    // ================================ others =========================================== //

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return Objects.equals(guiSettings, o.guiSettings)
                && Objects.equals(addressBookFilePath, o.addressBookFilePath);
    }
    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, addressBookFilePath);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings.toString());
        sb.append("\nLocal data file location : " + addressBookFilePath);
        return sb.toString();
    }



}
