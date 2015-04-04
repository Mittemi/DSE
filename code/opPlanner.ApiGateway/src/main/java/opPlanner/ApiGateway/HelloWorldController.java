package opPlanner.ApiGateway;

import HystrixCommands.CommandHelloWorld;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 04.04.2015.
 */
@RestController
public class HelloWorldController {

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public List<String> SayHallo() {
        final List<String> list = new ArrayList<>();
        list.add(new CommandHelloWorld("Lukas").execute());
        list.add(new CommandHelloWorld("Michi").execute());
        list.add(new CommandHelloWorld("Thomas").execute());
        return list;
    }


}
