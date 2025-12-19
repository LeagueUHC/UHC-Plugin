package fr.kiza.leagueuhc.core.api.drake.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour marquer une classe Drake comme enregistrable automatiquement.
 * Les classes annotées doivent hériter de {@link fr.kiza.leagueuhc.drakes.api.Drake}
 * et avoir un constructeur sans arguments.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DrakeEntry {

    /**
     * Priorité de spawn (plus bas = spawn en premier)
     */
    int priority() default 0;

    /**
     * Si true, ce drake n'est pas inclus dans le spawn automatique
     * (utile pour le Drake Ancestral)
     */
    boolean excludeFromSpawn() default false;
}
