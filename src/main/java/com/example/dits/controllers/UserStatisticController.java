package com.example.dits.controllers;

import com.example.dits.dto.TestStatisticByUser;
import com.example.dits.entity.User;
import com.example.dits.service.StatisticService;
import com.example.dits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserStatisticController {
    private final StatisticService statisticService;
    private final UserService userService;

    @GetMapping("/personal-statistic")
    public String personalStatistic(ModelMap model) {
        model.addAttribute("title", "Personal Statistic");
        return "user/personal-statistic";
    }

    @ResponseBody
    @GetMapping("/get-personal-statistics")
    public List<TestStatisticByUser> personalStatistic(int userId) {
        User user = userService.getUserById(userId);
        List<TestStatisticByUser> testStatistic = statisticService.getListOfTestsWithStatisticsByUser(user);
        testStatistic.sort(Comparator.naturalOrder());
        return testStatistic;
    }
}
