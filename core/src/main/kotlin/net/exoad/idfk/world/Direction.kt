package net.exoad.idfk.world

enum class Direction {
    NORTH, SOUTH, EAST, WEST;

    companion object {
        fun Direction.mapToTextureIndex(): Int {
            return when (this) {
                SOUTH -> 0
                WEST -> 1
                EAST -> 2
                NORTH -> 3
            }
        }
    }
}
