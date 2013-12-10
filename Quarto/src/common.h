#ifndef COMMON_H
#define COMMON_H

#include <QString>

const unsigned  PIECE_COUNT         = 16;
const unsigned  MATRIX_SIZE         = 4;
const unsigned  PLAYER_COUNT        = 2;
const int       MINUS_INF          = -1024;
const int       PLUS_INF           = 1024;
const unsigned  QUARTO_MOVES        = 16;
const unsigned  NOVICE_MOVES_COUNT  = 4;

enum player_e {
    RANDOM,
    NOVICE,
    MINIMAX,
    REMOTE,
    HUMAN
};

struct conn {
    quint16 port;
    QString host;
};

struct player_t {
    player_e type;
    unsigned minimax_level;
    conn host_port;
};

#define MAX(a,b) ((a) > (b)) ? a : b
#define MIN(a,b) ((a) > (b)) ? b : a

#endif // COMMON_H
