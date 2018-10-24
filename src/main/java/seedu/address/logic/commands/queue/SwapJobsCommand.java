package seedu.address.logic.commands.queue;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelMessageResult;
import seedu.address.model.job.JobName;

/**
 * Swaps two jobs in the maker manager queue
 */
public class SwapJobsCommand extends Command {
    public static final String COMMAND_WORD = "swapJobs";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Swaps jobs with jobname1 and jobname2"
            + " Parameters: "
            + PREFIX_NAME + "JOB NAME 1"
            + PREFIX_NAME + "JOB NAME 2"
            + "\nEXAMPLE: \n" + COMMAND_WORD + " "
            + PREFIX_NAME + "iDCP project"
            + PREFIX_NAME + "iDCP";

    public static final String MESSAGE_SUCCESS = "Jobs swapped: %1$s";

    private final JobName jobName1;
    private final JobName jobName2;

    /**
     * Creates an SwapJobCommand with two parameters
     * {@code jobName1} and {@code jobName2} where
     * these args are used to find the actual jobs in
     * the list and swap them based on their indexes
     */
    public SwapJobsCommand(String jobName1, String jobName2) {
        requireAllNonNull(jobName1, jobName2);
        this.jobName1 = new JobName(jobName1);
        this.jobName2 = new JobName(jobName2);
    }


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        ModelMessageResult modelMessageResult = model.swapJobs(jobName1, jobName2);
        modelMessageResult.printResult();
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, jobName1 + " and " + jobName2));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SwapJobsCommand // instanceof handles nulls
                && jobName1.equals(((SwapJobsCommand) other).jobName1)
                && jobName2.equals(((SwapJobsCommand) other).jobName2));
    }
}
