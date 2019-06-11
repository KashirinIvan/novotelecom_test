package novotelecom.novotelecom.controller;

import novotelecom.novotelecom.Repository.ScoreRepository;
import novotelecom.novotelecom.model.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private ScoreRepository scoreRepository;

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public String statusScore(@RequestParam int score, String button) {
        List<Score> scores = scoreRepository.findAll();
        Iterator<Score> iterator = scores.iterator();
        switch (button) {
            case "StatusButton":
                while (iterator.hasNext()) {
                    Score score1 = iterator.next();
                    if (score1.getScore() == score) {
                        return score + "-" + score1.getStatus();
                    }
                }
                break;
            case "LockButton":
                while (iterator.hasNext()) {
                    Score score1 = iterator.next();
                    if (score1.getScore() == score) {
                        score1.setStatus("LOCK");
                        scoreRepository.save(score1);
                        return score + " заблокирован";
                    }
                }
                break;
            case "UnlockButton":
                while (iterator.hasNext()) {
                    Score score1 = iterator.next();
                    if (score1.getScore() == score) {
                        score1.setStatus("UNLOCK");
                        scoreRepository.save(score1);
                        return score + " разблокирован";
                    }
                }
                break;
        }
        return null;
    }

    @RequestMapping(value = "/put", method = RequestMethod.POST)
    public String putScore(@RequestParam int score, int money, String button) {
        List<Score> scores = scoreRepository.findAll();
        Iterator<Score> iterator = scores.iterator();
        switch (button) {
            case "putMoney":
                while (iterator.hasNext()) {
                    Score score1 = iterator.next();
                    if (score1.getScore() == score) {
                        score1.setMoney(score1.getMoney() + money);
                        scoreRepository.save(score1);
                    }
                }
                break;
            case "checkMoney":
                while (iterator.hasNext()) {
                    Score score1 = iterator.next();
                    if (score1.getScore() == score) {
                        return "Баланс счета " + score + " = " + score1.getMoney();
                    }
                }
                break;
        }
        return "Баланс счета " + score + " пополнен на " + money;
    }

    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public String drawScore(@RequestParam int score, int money, String button) {
        List<Score> scores = scoreRepository.findAll();
        Iterator<Score> iterator = scores.iterator();
        switch (button) {
            case "drawMoney":
                while (iterator.hasNext()) {
                    Score score1 = iterator.next();
                    if (score1.getScore() == score) {
                        score1.setMoney(score1.getMoney() - money);
                        scoreRepository.save(score1);
                    }
                }
                break;
            case "checkMoney":
                while (iterator.hasNext()) {
                    Score score1 = iterator.next();
                    if (score1.getScore() == score) {
                        return "Баланс счета " + score + " = " + score1.getMoney();
                    }
                }
                break;
        }
        return "Баланс счета " + score + " уменьшен на " + money;
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public String transferScore(@RequestParam int score, int score1, int money) {
        List<Score> scores = scoreRepository.findAll();
        Iterator<Score> iterator = scores.iterator();
        boolean isScore = false;
        boolean isScore1 = false;
        Score scoreTemp = null;
        Score scoreTemp1 = null;
        while (iterator.hasNext()) {
            Score score2 = iterator.next();
            if (score2.getScore() == score) {
                scoreTemp = score2;
                isScore = true;
            } else if (score2.getScore() == score1) {
                scoreTemp1 = score2;
                isScore1 = true;
            }
            if (isScore && isScore1) {
                scoreTemp.setMoney(scoreTemp.getMoney() - money);
                scoreRepository.save(scoreTemp);
                scoreTemp1.setMoney(scoreTemp1.getMoney() + money);
                scoreRepository.save(scoreTemp1);
                return "Перевод выполнен";
            }
        }
        return "Перевод не выполнен";
    }

    @RequestMapping(value = "/addScore", method = RequestMethod.POST)
    public String addScore() {
        scoreRepository.save(new Score(1, 1200, "LOCK"));
        scoreRepository.save(new Score(2, 1500, "LOCK"));
        scoreRepository.save(new Score(3, 200, "UNLOCK"));
        scoreRepository.save(new Score(4, 490, "LOCK"));
        scoreRepository.save(new Score(5, 547, "UNLOCK"));
        scoreRepository.save(new Score(6, 9874, "LOCK"));
        scoreRepository.save(new Score(7, 5451, "UNLOCK"));
        scoreRepository.save(new Score(8, 58, "LOCK"));
        return "База заполнена, количество записей в базе = " + scoreRepository.count();
    }
}
