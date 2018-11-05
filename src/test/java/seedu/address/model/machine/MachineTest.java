package seedu.address.model.machine;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.job.exceptions.DuplicateJobException;
import seedu.address.testutil.testdata.ValidJobs;
import seedu.address.testutil.testdata.ValidMachines;

public class MachineTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void canAddJobToMachine() {
        Machine testMachine = ValidMachines.JJPRINTER;
        testMachine.addJob(ValidJobs.bumberbee);
    }

    @Test
    public void addDuplicateJobToMachine() {
        thrown.expect(DuplicateJobException.class);
        Machine testMachine = ValidMachines.JJPRINTER;
        testMachine.addJob(ValidJobs.IDCP);
        testMachine.addJob(ValidJobs.IDCP);
    }

    @Test
    public void isValidMachineName() {
        assertTrue(Machine.isValidName("JJPrinter2"));
    }

    @Test
    public void isInvalidMachineName() {
        assertTrue(Machine.isValidMachine("JJ JJ JJ "));
    }


}
