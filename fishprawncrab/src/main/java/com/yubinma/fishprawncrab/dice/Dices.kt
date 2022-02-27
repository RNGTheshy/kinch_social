package com.yubinma.fishprawncrab.dice


data class Dices(
    var firstDice: Int = (0..6).random(),
    var secondDice: Int = (0..6).random(),
    var thirdDice: Int = (0..6).random(),
)
