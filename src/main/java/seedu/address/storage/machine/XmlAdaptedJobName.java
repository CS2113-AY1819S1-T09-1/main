package seedu.address.storage.machine;

import javax.xml.bind.annotation.XmlValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Name;

/**
 * JAXB-friendly version of the JobName.
 */
public class XmlAdaptedJobName {

    @XmlValue
    private String jobName;

    /**
     * Constructs an XmlAdaptedJobName
     * This is the no-arg constructor that is required by JAXB
     */
    public XmlAdaptedJobName(){}

    /**
     * Constructs a {@code XmlAdaptedJobName} with the given {@code jobName}.
     */
    public XmlAdaptedJobName(String jobName) {
        this.jobName = jobName;
    }

    public XmlAdaptedJobName(Name name) {
    }

    /**
     * Converts this jaxb-friendly adapted jobname object into the model's Name object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted name
     */
    public Name toModelType() throws IllegalValueException {
        if (!Name.isValidName(jobName)) {
            throw new IllegalValueException((Name.MESSAGE_NAME_CONSTRAINTS));
        }
        return new Name(jobName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedJobName)) {
            return false;
        }

        return jobName.equals(((XmlAdaptedJobName) other).jobName);
    }
}
