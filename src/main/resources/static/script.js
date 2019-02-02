let me = '';
let myName = '';
let myTurn = false;
let currentTurn = '';
let players = {};

$(document).ready(() => {
    let source = new EventSource('/tictactoe');
    source.onmessage = (event) => {
        log(event.data);
        handleEvent(event);
    };
    source.onerror = (event) => {
        $('#error').append($('<div>').text(event.data));
    };

    $('#send').on('click', () => {
        const cell = $('button.cell:not(:disabled)').filter((_, el) => {
            return $(el).text() === currentTurn;
        });
        if (cell.length === 1) {
            const matches = cell.attr('id').match(/^cell(\d)-(\d)$/);
            const url = `/play/${matches[1]}/${matches[2]}`;
            $.ajax(url, {method: 'GET'});
        }
    });

    $('button.cell').each((_, button) => {
        $(button).on('click', (event) => {
            if (myTurn) {
                const target = $(event.target);
                $('button.cell:not(:disabled)').each((_, el) => {
                    $(el).text('');
                });
                target.text(currentTurn);
            }
        });
    });

    $('#sync').on('click', () => {
        synchronize();
    });

    $('#reset').on('click', () => {
        $.ajax('/restart', {method: 'GET'});
    });

    $('#logon').on('click', () => {
        logon($('#username').val());
    });

    $('#logoff').on('click', () => {
        $.ajax('/logoff', {
            method: 'GET',
            success: () => {
                me = '';
                myTurn = false;
            }
        });
    });

    $('#enableLog').on('change', (event) => {
        if ($(event.target).prop('checked')) {
            $('#logWrap').show();
        } else {
            $('#logWrap').hide();
        }
    });

    $('#clearLog').on('click', () => {
        $('#log').text('');
    });

    synchronize();

});

const handleEvent = (event) => {
    const eventData = JSON.parse(event.data);
    switch (eventData.type) {
        case 'join':
            players[eventData.player] = eventData.name;
            break;
        case 'turn':
            setCurrentTurn(eventData.player);
            break;
        case 'move':
            const el = $(`#cell${eventData.x}-${eventData.y}`);
            if (el.length) {
                el.text(eventData.player);
                el.prop('disabled', true);
            }
            break;
        case 'win':
            $('button.cell').each((_, button) => {
                $(button).prop('disabled', true);
            });
            break;
        case 'reset':
            clearAll();
            break;
    }
};

const logon = (username) => {
    myName = username;
    $.ajax('/logon?username=' + username, {
        method: 'GET',
        success: (_) => {
            synchronize();
        }
    });
};

const synchronize = () => {
    $.ajax('/sync', {
        method: 'GET',
        success: (state) => {
            console.log(state);
            me = state['me'];
            setCurrentTurn(state['turn']);
            players = state['players'];
            state['board'].forEach((row, i) => {
                row.forEach((column, j) => {
                    const cell = $(`#cell${i}-${j}`);
                    switch (column) {
                        case 'x':
                        case 'o':
                            cell.text(column).prop('disabled', true);
                            break;
                        default:
                        case '':
                            cell.val('');
                            break;
                    }

                });
            });

            if (me === 'x' || me === 'o') {
                $('#loginContainer').hide();
                $('#gameContainer').show();
            } else {
                $('#loginContainer').show();
                $('#gameContainer').hide();
            }
        }
    });
};

const setCurrentTurn = (player) => {
    $('#playerTurn').text(player);
    $('#playerName').text(players[player]);
    currentTurn = player;
    if (currentTurn === me) {
        myTurn = true;
        $('#send').prop('disabled', false);
    } else {
        myTurn = false;
        $('#send').prop('disabled', true);
    }
};

const clearAll = () => {
    $('button.cell').each((_, button) => {
        $(button).text('').prop('disabled', false);
    });

};

const log = (str) => {
    if ($('#logWrap').is(':visible')) {
        $('#log').append($('<div>').text(str));
    }
};
