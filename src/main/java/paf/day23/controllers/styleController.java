package paf.day23.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import paf.day23.repositories.BeerRepository;
import paf.day23.services.RedisService;
import paf.day23.models.Style;

@Controller
public class styleController {
    
    @Autowired
    private BeerRepository beerRepo;

    @Autowired
    private RedisService redisSvc;

    @GetMapping("/")
    public String getStyle(Model model){

        List<String> styles = beerRepo.getStyles();
        Style style = new Style();
        style.setStyleList(styles);

        model.addAttribute("Style", style);
        
        return "selectStyles";
    }

    @GetMapping("/breweries")
    public String displayBrewery(Model model, @ModelAttribute Style style){

        String beerStyle = style.getStyle();
        System.out.println(">>>>> Check style: " + style.getStyle());
        List<String> brewList = new LinkedList<>();

        if(redisSvc.hasBrew(beerStyle))
        {   
            System.out.println(" >>>>> this one use Redis Cache >>>>>");
            brewList = redisSvc.getBrew(beerStyle); 
            model.addAttribute("BrewList", brewList);
            model.addAttribute("Style", style);
            return "breweries";
        }

        brewList = beerRepo.getBreweries(style.getStyle());
        redisSvc.saveBrew(style.getStyle(), brewList);        

        model.addAttribute("Style", style);  // for checking only
        model.addAttribute("BrewList", brewList);
        return "breweries";
    }

}
