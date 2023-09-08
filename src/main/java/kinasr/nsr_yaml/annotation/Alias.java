package kinasr.nsr_yaml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to specify an alias for a field.
 *
 * <p>The annotation can be used at the field level to specify an alternative name for the field when
 * parsing YAML data. The alternative name specified using this annotation takes precedence over the
 * actual field name.
 *
 * <p>This annotation has runtime retention policy and can be used on fields.
 *
 * @see Retention
 * @see Target
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Alias {

    /**
     * The alias name for the field.
     *
     * @return The alias name.
     */
    String value();
}
