package wsg.lol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import wsg.lol.service.intf.ChampionService;

/**
 * todo
 *
 * @author EastSunrise
 */
@Controller
@RequestMapping("/lol/champion")
public class ChampionController extends BaseController {

    private ChampionService championService;

    @RequestMapping("/index")
    public String championList() {
        return templatePath("champions");
    }

    @RequestMapping("/individual")
    public String getChampionInfo(String name, Model model) {
        return templatePath("individual");
    }

    @Override
    String templatePath(String fileName) {
        return "champion/" + fileName;
    }

    @Autowired
    public void setChampionService(ChampionService championService) {
        this.championService = championService;
    }
}