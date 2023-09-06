package com.example.bonus.domain;

import lombok.Data;

import java.util.Map;

@Data
public class RewardResponse {
    private Map<String, Integer> monthlyRewards;
    private int totalRewards;
}

