package br.com.manzano.challenge.n26.controller;

import br.com.manzano.challenge.n26.util.StatisticManager;
import br.com.manzano.challenge.n26.model.StatisticsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    StatisticManager statisticManager;

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StatisticsInfo> statistics(HttpServletResponse response) {
        // Allow cors
        response.addHeader("Access-Control-Allow-Methods", "GET");
        response.addHeader("Access-Control-Allow-Origin", "*");

        StatisticsInfo statisticsInfo = new StatisticsInfo();
        statisticsInfo.setSum(statisticManager.getSum());
        statisticsInfo.setAvg(statisticManager.getAvg());
        statisticsInfo.setMin(statisticManager.getMin());
        statisticsInfo.setMax(statisticManager.getMax());
        statisticsInfo.setCount(statisticManager.getCount());

        return ResponseEntity.ok(statisticsInfo);
    }
}
