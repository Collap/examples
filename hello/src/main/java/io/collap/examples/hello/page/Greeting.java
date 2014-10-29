package io.collap.examples.hello.page;

import io.collap.bryg.environment.Environment;
import io.collap.bryg.model.BasicModel;
import io.collap.bryg.model.Model;
import io.collap.controller.ModuleController;
import io.collap.controller.communication.Response;
import io.collap.controller.provider.BrygDependant;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * The Greeting controller can be registered at any dispatcher.
 * The GET request method asks the user for a name.
 * The POST request method requires the user to send a name. The controller then greets the user.
 *
 * Since this controller is a BrygDependant, the setBryg method (declared in the BrygDependant interface)
 * is called when an instance of this controller is created.
 */
public class Greeting extends ModuleController implements BrygDependant {

    private Environment bryg;

    /**
     * This method asks the user for a name.
     */
    @Override
    public void doGet (Response response) throws IOException {
        /* For more detailed explanations about these methods, check the doPost method below. */

        Model model = new BasicModel ();

        bryg.getTemplate ("NameForm").render (response.getContentWriter (), model);
        bryg.getTemplate ("NameForm_head").render (response.getHeadWriter (), model);
    }

    /**
     * This method greets the user, or says that it doesn't know the user if no name is
     * supplied through a form.
     */
    @Override
    public void doPost (Response response) throws IOException {
        /* The @Nullable indicates that the name variable may be null. */
        @Nullable String name = request.getStringParameter ("name");

        /* A model should be created with the Environment's createModel method
         * to include global variables defined in the module. */
        Model model = new BasicModel ();

        /* The name argument is set here.
         * A variable with the value 'null' and an undefined variable are essentially the same when
         * it comes to bryg models. Since the name parameter is optional, we can skip the null check here. */
        model.setVariable ("name", name);

        /* The first time a template is received from the environment, the environment compiles the template JIT.
         * This means that the first execution takes much longer, but the template is cached after this first compilation
         * for the rest of the Environment's lifespan. */
        bryg.getTemplate ("Greeting").render (response.getContentWriter (), model);
        bryg.getTemplate ("Greeting_head").render (response.getHeadWriter (), model);
    }

    @Override
    public void setBryg (Environment environment) {
        this.bryg = environment;
    }

}
