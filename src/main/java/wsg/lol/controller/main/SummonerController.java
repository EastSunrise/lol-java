package wsg.lol.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * wsg
 *
 * @author EastSunrise
 */
@Controller
@RequestMapping("/lol/summoner")
public class SummonerController extends BaseController {

    @RequestMapping("/index")
    public String homePage() {
        return templatePath("index");
    }

    @RequestMapping("/search")
    public String searchSummoner(String name, Model model) {
        return "";
    }

    @Override
    String templatePath(String fileName) {
        return "summoner/" + fileName;
    }
}