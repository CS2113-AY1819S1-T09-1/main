package seedu.address.model.job;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.address.testutil.builders.JobBuilder;
import seedu.address.testutil.builders.MachineBuilder;
import seedu.address.testutil.builders.PersonBuilder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.testutil.testdata.TypicalJobs.newProject;
import static seedu.address.testutil.testdata.ValidJobs.IDCP;

public class JobTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Job job = new JobBuilder().build();
        thrown.expect(UnsupportedOperationException.class);
        job.getTags().remove(0);
    }

    @Test
    public void isSameJob() {

        // same object -> returns true
        assertTrue(IDCP.isSameJob(IDCP));

        // null -> returns false
        assertFalse(IDCP.isSameJob(null));

        // different name -> returns false
        Job editedIDCP = new JobBuilder().withName(VALID_JOBNAME_PROONE).build();
        assertFalse(IDCP.isSameJob(editedIDCP));

        /* different owner -> returns false */
        editedIDCP = new JobBuilder().withOwner(new PersonBuilder().build()).build();
        assertFalse(IDCP.isSameJob(editedIDCP));

        // different machine -> returns false
        editedIDCP = new JobBuilder().withMachine(new MachineBuilder().build()).build();
        assertFalse(IDCP.isSameJob(editedIDCP));

        // different duration -> returns false
        editedIDCP = new JobBuilder().withDuration(Float.valueOf(VALID_JOBDURATION_FIVE)).build();
        assertFalse(IDCP.isSameJob(editedIDCP));

        // same name, same machine, same owner, different attributes -> returns true
        editedIDCP = new JobBuilder(IDCP).withPriority(VALID_JOBPRIORITY_NORMAL).withJobNote(VALID_JOBNOTE_ANOTHERIDCP)
                .withTags(VALID_TAG_HUSBAND).withStatus(VALID_JOBSTATUS_CANCELLED).build();
        assertTrue(IDCP.isSameJob(editedIDCP));

        // same name, same machine, same owner, same priority, different attributes -> returns true
        editedIDCP = new JobBuilder(IDCP).withJobNote(VALID_JOBNOTE_ANOTHERIDCP)
                .withTags(VALID_TAG_HUSBAND).withStatus(VALID_JOBSTATUS_CANCELLED).build();
        assertTrue(IDCP.isSameJob(editedIDCP));

        // same name, same machine, same owner, same priority, same status, different attributes -> returns true
        editedIDCP = new JobBuilder(IDCP).withJobNote(VALID_JOBNOTE_ANOTHERIDCP)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(IDCP.isSameJob(editedIDCP));

    }

    @Test
    public void equals() {
        // same values -> returns true
        Job iDCPCopy = new JobBuilder(IDCP).build();
        assertTrue(IDCP.equals(iDCPCopy));

        // same object -> returns true
        assertTrue(IDCP.equals(IDCP));

        // null -> returns false
        assertFalse(IDCP.equals(null));

        // different type -> returns false
        assertFalse(IDCP.equals(5));

        // different job -> returns false
        assertFalse(IDCP.equals(newProject));

        // different name -> returns false
        Job editedIDCP = new JobBuilder(IDCP).withName(VALID_JOBNAME_PROONE).build();
        assertFalse(IDCP.equals(editedIDCP));

        // different machine -> returns false
        editedIDCP = new JobBuilder(IDCP).withMachine(new MachineBuilder().build()).build();
        assertFalse(IDCP.equals(editedIDCP));

        // different owner -> returns false
        editedIDCP = new JobBuilder(IDCP).withOwner(new PersonBuilder().build()).build();
        assertFalse(IDCP.equals(editedIDCP));

        // different added time -> returns false
        editedIDCP = new JobBuilder(IDCP).withAddedTime(VALID_JOBADDEDTIME_SOMEDAY).build();
        assertFalse(IDCP.equals(editedIDCP));

    }


    //TODO: test for hasHigherPriority
    /*
    @Test
    public void hasHigherPriority() {

    }
    */
}
