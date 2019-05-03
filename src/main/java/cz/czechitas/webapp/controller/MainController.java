package cz.czechitas.webapp.controller;

import java.util.*;
import java.util.regex.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import cz.czechitas.webapp.entity.*;
import cz.czechitas.webapp.logic.*;
import cz.czechitas.webapp.persistence.*;

@Controller
public class MainController {

    private PexesoService pexesoService;

    public MainController(PexesoRepository pexesoRepository) {
        pexesoService = new PexesoService(pexesoRepository);
    }

    @RequestMapping("/")
    public ModelAndView showIndex() {
        return new ModelAndView("index");
    }

    @RequestMapping ("/game-list.html")
    public ModelAndView showGameList() {
        List<GameBoard> gameList = pexesoService.findAllBoards();
        ModelAndView moavi = new ModelAndView("game-list");
        moavi.addObject("listOfAllGames", gameList);
        return moavi;
    }

    @RequestMapping(value = "/newtable.html")
    public String showNewTable() {
        GameBoard board = pexesoService.createBoard();
        return "redirect:/table.html?id=" + board.getId();
    }

    @RequestMapping(value = "/table.html", method = RequestMethod.GET)
    public ModelAndView showTable(@RequestParam("id") Long boardId) {
        GameBoard board = pexesoService.findBoard(boardId);
        ModelAndView moavi = new ModelAndView("table");
        moavi.addObject("currentBoard", board);
        return moavi;
    }

    @RequestMapping(value = "/table.html", method = RequestMethod.POST)
    public String processTable(@RequestParam("id") Long boardId, @RequestParam Map<String, String> allParams) {
        int clickedCardNumber = findClickedCardNumber(allParams.keySet());           //proč se převádí mapa na Set?
        if (clickedCardNumber != -1) {
            pexesoService.makeMove(boardId, clickedCardNumber);
        }
        return "redirect:/table.html?id=" + boardId;
    }

    @RequestMapping(value = "/delete/{gameId}.html", method=RequestMethod.POST)
    public ModelAndView deleteTable(@PathVariable ("gameId") Long id) {
        pexesoService.deleteBoard(id);
        return new ModelAndView("redirect:/game-list.html");
    }

    private int findClickedCardNumber(Collection<String> parameterNames) {
        Pattern regex = Pattern.compile("clickedCard(\\d+).+");
        for (String paramName : parameterNames) {
            Matcher matcher = regex.matcher(paramName);
            if (matcher.matches()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        return -1;
    }

    @ExceptionHandler(GameNotFoundException.class)
    public String zacniNovouHruPokudIdNeexistuje() {
        return "redirect:/newtable.html";
    }
}
