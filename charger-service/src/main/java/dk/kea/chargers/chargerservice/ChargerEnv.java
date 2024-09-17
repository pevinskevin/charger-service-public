package dk.kea.chargers.chargerservice;

import java.net.URI;
import java.util.Optional;

public class ChargerEnv {
    /**
     * Use `ChargerEnv.withDefaultStr("VAR_NAME", "default value")` to get the variable VAR_NAME
     * from the environment. If the variable VAR_NAME is not defined, use "default value" as the
     * content of the variable instead.
     *
     * @param envVar The variable's name
     * @param defaultValue The fallback value when variable is missing
     * @return Either the variable's content, or the fallback default value
     */
    public static String withDefaultStr(String envVar, String defaultValue) {
        return Optional.ofNullable(System.getenv(envVar)).orElse(defaultValue);
    }

    /**
     * Use `ChargerEnv.withDefaultInt("VAR_NAME", 42)` to get the variable VAR_NAME from the
     * environment. If the variable VAR_NAME is not defined, or it cannot be parsed as an int,
     * use the provided default value.
     *
     * @param envVar The variable's name
     * @param defaultValue The fallback value when variable is missing
     * @return Either the variable's content, or the fallback default value
     */
    public static int withDefaultInt(String envVar, int defaultValue) {
        var envValue = System.getenv(envVar);
        if (envValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(envValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Use `ChargerEnv.withDefaultUri("VAR_NAME", ...)` to get the variable VAR_NAME from the
     * environment. If the variable VAR_NAME is not defined, or it cannot be parsed as a URI,
     * use the provided default URI.
     *
     * @param envVar The variable's name
     * @param defaultUri The fallback value when variable is missing
     * @return Either the variable's content, or the fallback default value
     */
    public static URI withDefaultUri(String envVar, URI defaultUri) {
        try {
            return URI.create(withDefaultStr(envVar, defaultUri.toString()));
        } catch (IllegalArgumentException e) {
            return defaultUri;
        }
    }
}
