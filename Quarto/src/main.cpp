#include <QtCore/QCoreApplication>
#include <QTimer>
#include <iostream>

#include "game.h"
#include "common.h"
#include "optionparser/optionparser.h"

// states that can occur by changing command line arguments
enum status_t {
    E_OK,
    E_HEADLESS,
    E_HELP,
    E_CMD_ERROR
};

// settings of whole program
struct settings_t {
    status_t status;
    player_t players[PLAYER_COUNT];
    unsigned repetitions;
};

// possible command line options
enum optionIndex {
    OI_UNKNOWN,
    OI_HELP,
    OI_HEADLESS,
    OI_RANDOM,
    OI_NOVICE,
    OI_MINIMAX,
    OI_REMOTE,
    OI_HUMAN,
    OI_REPETITIONS
};

// option parser stuff
struct Arg: public option::Arg {
    static option::ArgStatus Required(const option::Option& option, bool)
    {
        return option.arg == 0 ? option::ARG_ILLEGAL : option::ARG_OK;
    }
};

const option::Descriptor usage[] = {
    {OI_UNKNOWN, 0, "",  "", option::Arg::None,     "USAGE: Quatro [OPTIONS] PLAYER PLAYER \n\n"
                                                 "Options:" },
    {OI_HELP,    0, "", "help", option::Arg::None, "  --help  \tPrint this text and exit." },
    {OI_REPETITIONS,0, "", "repetitions", Arg::Required, "  --repetitions  \tHow many games should be played.\n\n"
                                                 "Players:"},
    {OI_RANDOM,  0, "", "random", option::Arg::None, "  --random   \tRandom player." },
    {OI_NOVICE,  0, "", "novice", option::Arg::None, "  --novice   \tNovice player." },
    {OI_MINIMAX, 0, "", "minimax", Arg::Required, "  --minimax L  \tMinimax player with given depth level L." },
    {OI_REMOTE,  0, "", "remote", Arg::Required, "  --remote IP:PORT  \tRemote player on given ip & port." },
    {OI_HUMAN,   0, "", "human", option::Arg::None, "  --human   \tHuman player." },
    {OI_UNKNOWN, 0, "" ,  ""   , option::Arg::None, "\nExamples:\n"
                                                 "  Quatro --random --random\n"
                                                 "  Quatro --random --minimax 4\n" },
      {0,0,0,0,0,0}
};

// Declarations
settings_t parse_cmd_params(int argc, char *argv[]);

/**
 * Main program function
 */
int main(int argc, char *argv[])
{
    settings_t settings;
    QCoreApplication app(argc, argv);

    settings = parse_cmd_params(argc, argv);
    if (settings.status == E_HELP) {
        option::printUsage(std::cout, usage);
        return 0;
    } else if (settings.status == E_CMD_ERROR) {
        std::cerr << "Error occured when parsing command line parameters.\n\n";
        option::printUsage(std::cout, usage);
        return 1;
    } else {

    }

    // create the main class
    Game game(settings.repetitions, settings.players[0], settings.players[1], &app);

    // connect up the signals
    QObject::connect(&game,   SIGNAL(finished()),
                     &app,    SLOT(quit()));
    QObject::connect(&app,    SIGNAL(aboutToQuit()),
                     &game,   SLOT(aboutToQuitApp()));

    // Starts the execution after 10ms in the Game.run() routine.
    QTimer::singleShot(10, &game, SLOT(run()));

    int retval = 0;
    try {
        retval = app.exec();
    } catch (const char * excpt) {
        qDebug() << excpt;
    }

    return retval;
}

/**
 * @brief parse_cmd_params creates structure for game settings according to given
 * parameters
 * @param argc
 * @param argv
 * @return desired structure
 */
settings_t parse_cmd_params(int argc, char *argv[])
{
    settings_t settings;

    settings.status = E_OK;
    settings.repetitions = 1;

    // skip program name argv[0] if present
    argc -= (argc > 0);
    argv += (argc > 0);

    option::Stats  stats(usage, argc, argv);
    option::Option options[stats.options_max], buffer[stats.buffer_max];
    option::Parser parse(usage, argc, argv, options, buffer);

    if (parse.error()) { // some error occured during parsing
        settings.status = E_CMD_ERROR;
        return settings;
    }

    if (options[OI_HELP] || argc == 0) { // call for help or no parameters
        settings.status = E_HELP;
        return settings;
    }

    // write unknown parameters
    for (option::Option* opt = options[OI_UNKNOWN]; opt; opt = opt->next())
        std::cout << "Unknown option: " << opt->name << "\n";

    // write non options parameters
    for (int i = 0; i < parse.nonOptionsCount(); ++i)
        std::cout << "Non-option #" << i << ": " << parse.nonOption(i) << "\n";

    // check if everything is ok
    unsigned players_count = options[OI_HUMAN].count()
            + options[OI_MINIMAX].count()
            + options[OI_NOVICE].count()
            + options[OI_RANDOM].count()
            + options[OI_REMOTE].count();

    if (options[OI_UNKNOWN].count() > 0 || parse.nonOptionsCount() > 0 || players_count != 2) {
        settings.status = E_CMD_ERROR;
        return settings;
    }

    unsigned player_index = 0;

    // fill up settings structure
    for (int i = 0; i < parse.optionsCount(); ++i) {
        option::Option& opt = buffer[i];
        switch (opt.index()) {
            case OI_REPETITIONS:
                settings.repetitions = atoi(opt.arg);
                break;
            case OI_HEADLESS:
                settings.status = E_HEADLESS;
                break;
            case OI_RANDOM:
                settings.players[player_index++].type = RANDOM;
                break;
            case OI_NOVICE:
                settings.players[player_index++].type = NOVICE;
                break;
            case OI_MINIMAX:
                settings.players[player_index].minimax_level = atoi(opt.arg);
                settings.players[player_index++].type = MINIMAX;
                break;
            case OI_REMOTE:
//                settings.players[player_index].host_port = QString(opt.arg);
                settings.players[player_index].host_port.host = QString(opt.arg).left(QString(opt.arg).indexOf(':'));
                settings.players[player_index].host_port.port = atoi(QString(opt.arg).right(QString(opt.arg).size() - QString(opt.arg).indexOf(':') - 1).toStdString().c_str());
                settings.players[player_index++].type = REMOTE;
                break;
            case OI_HUMAN:
                settings.players[player_index++].type = HUMAN;
                break;
            default:
                throw "parse_cmd_params: unrecongized player type";
                break;
        }
    }

    return settings;
}
