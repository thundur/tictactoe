let me = 'x';
let myName = '';
let currentTurn = '';
let players = {};

$(document).ready(() => {
    let source = new EventSource('/tictactoe');
    source.onmessage = function(event) {
        log(event.data);
        handleEvent(event);
    };
    source.onerror = function(event) {
        $('#error').append($('<div>').text(event.data));
    };

    $('#send').on('click', () => {
        const cell = $('button.cell:not(:disabled)').filter((_, el) => {
            return $(el).text().toLowerCase() === currentTurn;
        });
        if(cell.length === 1) {
            const matches = cell.attr('id').match(/^cell(\d)-(\d)$/);
            const url = `/play/${matches[1]}/${matches[2]}`;
            $.ajax(url, {method: 'GET'});
        }
    });

    $('button.cell').each((_, button) => {
        $(button).on('click', (event) => {
            const target = $(event.target);
            $('button.cell:not(:disabled)').each((_, el) => {
                $(el).text('');
            });
            target.text(currentTurn);
        });
    });

    $('#sync').on('click', () => {
        synchronize();
    });

    $('#reset').on('click', () => {
        $.ajax('/restart', { method: 'GET' });
    });

    $('#logon').on('click', () => {
       logon($('#username').val());
    });

    synchronize();
});

const handleEvent = (event) => {
    const eventData = JSON.parse(event.data);
    switch(eventData.type) {
        case 'join':
            players[eventData.player.toLowerCase()] = eventData.name;
            break;
        case 'turn':
            setCurrentTurn(eventData.player.toLowerCase());
            break;
        case 'move':
            const el = $(`#cell${eventData.x}-${eventData.y}`);
            if(el.length) {
                el.text(eventData.player.toLowerCase());
                el.prop('disabled', true);
            }
            break;
        case 'win':
            $('button.cell').each(() => {
                $(this).prop('disabled', true);
            });
            break;
        case 'reset':
            clearAll();
            break;
    }
};

const logon = (username) => {
    myName = username;
    $.ajax('/logon?username='+ username, {
        method: 'GET',
        success: synchronize
    });
};

const synchronize = () => {
    $.ajax('/sync', {
        method: 'GET',
        success: (data) => {
            const parts = data.split('\n');
            for(const i of parts.keys()) {
                let subparts = parts[i].trim().split(' ');
                if(subparts.length === 3) {
                    for(const j of subparts.keys()) {
                        const cell = $(`#cell${i}-${j}`);

                        switch(subparts[j]) {
                            case 'EMPTY':
                                cell.val('');
                                break;
                            case 'X':
                            case 'O':
                                cell.text(subparts[j].toLowerCase()).prop('disabled', true);
                                break;
                        }
                    }
                } else if(subparts.length === 1) {
                    setCurrentTurn(subparts[0].toLowerCase());
                } else {
                    log('ERROR: '+ subparts)
                }
            }
        }
    });
};

const setCurrentTurn = (player) => {
    $('#playerTurn').text(player);
    $('#playerName').text(players[player]);
    currentTurn = player;
};

const clearAll = () => {
    $('button.cell').each(function() {
        $(this).text('').prop('disabled', false);
    });

};

const log = (str) => {
    $('#log').append($('<div>').text(str));
};
