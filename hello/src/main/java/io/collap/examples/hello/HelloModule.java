package io.collap.examples.hello;

import io.collap.bryg.EnvironmentConfigurator;
import io.collap.bryg.EnvironmentCreator;
import io.collap.bryg.ModuleSourceLoader;
import io.collap.bryg.compiler.Configuration;
import io.collap.bryg.compiler.resolver.ClassResolver;
import io.collap.bryg.environment.Environment;
import io.collap.bryg.loader.SourceLoader;
import io.collap.bryg.model.GlobalVariableModel;
import io.collap.controller.Dispatcher;
import io.collap.controller.ProviderControllerFactory;
import io.collap.controller.provider.BrygProvider;
import io.collap.examples.hello.page.Greeting;
import io.collap.plugin.Module;

import javax.annotation.Nullable;

public class HelloModule extends Module implements BrygProvider, EnvironmentConfigurator {

    /* The version and artifact name are important for the template source loader to find the sources.
     * The name and version must NOT differ from the values defined in this module's build.gradle. */
    public static final String VERSION = "0.1";
    public static final String ARTIFACT_NAME = "examples-hello-" + VERSION;

    private Environment bryg;

    /**
     * The initialize method is called in stage 4 of the plugin initialization process (Refer to the Overview in the wiki).
     */
    @Override
    public void initialize () {
        /* The bryg environment must be created before it can be used.
         * Each module has a different environment, to prevent namespace pollution. */
        bryg = new EnvironmentCreator (collap, this).create ();

        /* The root dispatcher is the dispatcher that corresponds to the base path of collap.
         * All other dispatchers and controllers are directly or indirectly registered here. */
        Dispatcher rootDispatcher = collap.getRootDispatcher ();
        Dispatcher examplesDispatcher = new Dispatcher (collap); // TODO: Only create and register the dispatcher if it doesn't already exist,
                                                                 // otherwise: get the dispatcher and use it (Requires API change).

        /* We just created an 'examples' dispatcher, which now needs to be registered at the root to be callable. */
        rootDispatcher.registerDispatcher ("examples", examplesDispatcher);

        /* This line registers a new controller factory with the Greeting controller and the 'hello' name
         * at the 'examples' dispatcher. The ProviderControllerFactory expects zero or more types that implement
         * the Provider interface. Since this module class already provides a bryg environment (and
         * implements the right interfaces), we can just pass 'this'. */
        examplesDispatcher.registerControllerFactory ("hello",
                new ProviderControllerFactory (Greeting.class, this));
    }

    /**
     * This is called when collap is shut down.
     *
     * TODO: Clarify order of destruction.
     */
    @Override
    public void destroy () {

    }

    /**
     * This method is primarily used to add annotated classes to the Hibernate configuration.
     * This method is called before the plugin is initialized.
     */
    @Override
    public void configureHibernate (org.hibernate.cfg.Configuration configuration) {

    }


    // Override from EnvironmentConfigurator

    /**
     * This ModuleSourceLoader allows the Environment to find the template files as resources in the cache.
     */
    @Override
    public SourceLoader getSourceLoader () {
        return new ModuleSourceLoader (this);
    }

    @Override
    public void configureConfiguration (Configuration configuration) {

    }

    @Override
    public void configureClassResolver (ClassResolver classResolver) {

    }

    @Override
    public void configureGlobalVariableModel (GlobalVariableModel globalVariableModel) {

    }

    @Override
    public @Nullable String getArtifactName () {
        return ARTIFACT_NAME;
    }


    // Override from BrygProvider

    @Override
    public Environment getBryg () {
        return bryg;
    }

}
