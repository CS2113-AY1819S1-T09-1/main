package seedu.address.ui.job;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.job.Job;
import seedu.address.model.machine.Machine;
import seedu.address.ui.UiPart;
import seedu.address.ui.machine.MachineCard;

import java.util.ArrayList;
import java.util.List;

/**
 * An UI component that displays information of a {@code Job}.
 */
public class JobCard extends UiPart<Region> {

    private static final String FXML = "JobListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Job job;

    @FXML
    private HBox cardPane;
    @FXML
    private Label jobName;
    @FXML
    private FlowPane jobInformation;
    @FXML
    private FlowPane details;
    @FXML
    private Label status;
    @FXML
    private FlowPane tags;
    @FXML
    private Label jobDescription;
    @FXML
    private Label jobDuration;

    public JobCard(Job job, int displayIndex) {
        super(FXML);
        this.job = job;
        jobName.setText(job.getJobName().fullName);

        Label machineNameLabel = new Label("Machine: " + job.getMachine().getName().fullName);
        Label informationLabel = new Label("Added by " + job.getOwner().getName().fullName + " at "
                + job.getAddedTime());
        Label priorityLabel = new Label("Priority: " + job.getPriority().toString());
        Label statusLabel = new Label("Status: " + job.getStatus().toString());
        details.getChildren().add(machineNameLabel);
        details.setHgap(2);
        jobInformation.getChildren().add(informationLabel);
        jobInformation.setHgap(2);

        tags.getChildren().add(priorityLabel);
        tags.getChildren().add(statusLabel);

        job.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        jobDescription.setText(job.getJobNote().toString());
        jobDuration.setText("ETC: "+ (job.getDuration() + job.getMachine().getTotalDuration()) + (" hour(s)."));


        //TODO dont know why the color is not changing...
        if (job.requestDeletion()) {
            Label requestDeletionLabel = new Label("Requested Deletion");
            requestDeletionLabel.setBackground(new Background(
                    new BackgroundFill(
                            Paint.valueOf("#dd0404"),
                            new CornerRadii(2),
                            new Insets(0))));
            requestDeletionLabel.setTextFill(Paint.valueOf("#F00"));
            tags.getChildren().add(requestDeletionLabel);


        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MachineCard)) {
            return false;
        }

        // state check
        JobCard jobCard = (JobCard) other;
        return job.equals(jobCard.job);
    }
}
