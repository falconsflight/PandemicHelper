package falcon.jacob.pandemichelper

enum class EpidemicLevel(val value: Int) {
    BEGINNER(4),
    ADEPT(5),
    EXPERT(6);

    companion object {
        fun fromInt(value: Int) = EpidemicLevel.values().first { it.value == value }
    }

}