#include <QDebug>
#include "game.h"
#include "playerremote.h"
#include "playerrandom.h"
#include "playernovice.h"
#include "playerminimax.h"
#include "playerhuman.h"
#include <iostream>
#include <typeinfo>
#include <cassert>

/**
 * Constructor
 *
 * @param parent a pointer to the parent
 */
Game::Game(unsigned repetitions, player_t & p1, player_t & p2, QObject *parent) :
    QObject(parent),
    repetitions(repetitions),
    playerCounter(0),
    turn(NULL)
{
    // Get the instance of the main application
    app = QCoreApplication::instance();

    // create essential objects

    board   = new Board(this);
    player1 = createPlayer(p1);
    player2 = createPlayer(p2);

    PlayerRemote *remote1 = dynamic_cast<PlayerRemote *>(player1);
    PlayerRemote *remote2 = dynamic_cast<PlayerRemote *>(player2);

    if ((remote1 && remote1->hasFirstTurn()) || (remote2 && !remote2->hasFirstTurn()))
        // remote player chooses who starts
        starts = player1;
    else
        starts = player2;
}

/**
 * Main game function. Creates the objects of Player, Board, Piece.
 * Controls the game state.
 */
void Game::run()
{
    // wins, draws, looses for player1
    unsigned resultsTable[3] = {0, 0, 0};

    for (unsigned i = 0; i < repetitions; ++i) {
        turn = starts;
        Piece* piece = turn->choosePiece();
        board->deletePieceFromStock(piece);
        std::cout << turn->getName().toStdString() << " choosed first piece: " << piece->toString().toStdString() << std::endl;
        try {
            while (!board->checkVictory() && piece != NULL) {
                turn = getOpponent(turn);
                std::cout << "----------------------------------------------------------" << std::endl;
                std::cout << turn->getName().toStdString() << "'s turn, play with piece: " << piece->toString().toStdString() << std::endl;
                piece = turn->move(piece);
                std::cout << turn->getName().toStdString() << " played the piece and choosed a piece for opponent." << std::endl;
                std::cout << "You can see the position below" << std::endl;
                board->printMatrix();
                board->printStock();
            }
        } catch (const char * excpt) {
            qDebug() << excpt;
            exit(1);
        }

        if (isPlayerRemote(getOpponent(turn))) {
            PlayerRemote *p =  dynamic_cast<PlayerRemote *>(getOpponent(turn));
            p->sendPosition(board->lastMove);
        }

        if (board->checkVictory()) { // someone has won
            unsigned index = turn == player1 ? 0 : 2;
            resultsTable[index]++;
            std::cout << turn->getName().toUtf8().constData() << " has won!" << std::endl;
        } else { // draw
            if (board->getStock().size() != 0) {
                qDebug() << "This can never happen!";
                exit(1);
            }
            resultsTable[1]++;
            std::cout << "It is a draw!" << std::endl;
        }

        if ((isPlayerRemote(player1)) || isPlayerRemote(player2)) { // remote game, check if repetition;
            Player *r = isPlayerRemote(player1) ? player1 : player2;
            PlayerRemote *remote = dynamic_cast<PlayerRemote *>(r);
            QString data = remote->getLineFromSocket();

            while (data != "Please reset yourself for the next game.\n") {
                data = remote->getLineFromSocket();
            }
        }

        board->reset();
        player1->reset();
        player2->reset();
    }

    printStatistics(resultsTable);
    quit();
}
/**
 * Quits the application
 */
void Game::quit() {
    emit finished();
}


/**
 * @brief Game::getOpponent
 * @param player
 * @return returns opponent (second player) of given player
 */
Player *Game::getOpponent(Player *player)
{
    if (player == player1)
        return player2;
    if (player == player2)
        return player1;
    else
        throw "getOpponent: invalid given player";  // TODO
}

/**
 * @brief Game::createPlayer creates a player according to given structure
 * @param player
 * @return
 */
Player *Game::createPlayer(struct player_t &player)
{
    playerCounter++;
    Player *retval = NULL;
    switch (player.type) {
    case RANDOM:
        retval = new PlayerRandom(QString(playerCounter+'0').append("-Random"), board, this);
        break;
    case NOVICE:
        retval = new PlayerNovice(QString(playerCounter+'0').append("-Novice"), board, this);
        break;
    case MINIMAX:
        retval = new PlayerMiniMax(QString(playerCounter+'0').append("-Minimax-").append(QString("%1").arg(player.minimax_level)), player.minimax_level, board, this);
        break;
    case REMOTE:
        retval = new PlayerRemote(QString(playerCounter+'0').append("-Remote"), player.host_port.host, player.host_port.port, board, this);
        break;
    case HUMAN:
        retval = new PlayerHuman(QString(playerCounter+'0').append("-Human"), board, this);
        break;
    default:
        throw "NOT IMPLEMENTED";
        break;
    }

    return retval;
}

/**
 * @brief Game::isPlayerRemote checks if player is remote player
 * @param p
 * @return
 */
bool Game::isPlayerRemote(Player *p)
{
    assert(p != NULL);

    PlayerRemote *remote = dynamic_cast<PlayerRemote *>(p);
    return remote != NULL;
}

/**
 * @brief Game::printStatistics prints given statistics
 * @param resultsTable is a array of 3 items player1wins - draws - player2wins
 */
void Game::printStatistics(unsigned *resultsTable)
{
    std::cout << "---------------------------------------\n";
    std::cout << repetitions << " games played\n";
    std::cout << player1->getName().toStdString() << " had won " << resultsTable[0] << " times\n";
    std::cout << player2->getName().toStdString() << " had won " << resultsTable[2] << " times\n";
    std::cout << "Draws: " << resultsTable[1] << " times\n";
    std::cout << "---------------------------------------\n";
}

/**
 * Last cleaning before quitting.
 */
void Game::aboutToQuitApp()
{
    // cleaning
}
