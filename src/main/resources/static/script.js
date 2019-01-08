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
        const cell = $('input:not(:disabled)').filter(function() {
            return this.value.toLowerCase() === me;
        });
        if(cell.length === 1) {
            const matches = cell.attr('id').match(/^cell(\d)-(\d)$/);
            const url = `/play/${matches[1]}/${matches[2]}`;
            $.ajax(url, {method: 'GET'});
        }
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
                el.val(eventData.player.toLowerCase());
                el.prop('disabled', true);
            }

            break;
        case 'win':
            $('input.cell').each(() => {
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
                                cell.val(subparts[j].toLowerCase()).prop('disabled', true);
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
    console.log(player);
    console.log(players);
    console.log(players[player]);
    $('#playerName').text(players[player]);
    currentTurn = player;
};

const clearAll = () => {
    $('input.cell').each(function() {
        $(this).val('').prop('disabled', false);
    });

};

const log = (str) => {
    $('#log').append($('<div>').text(str));
};
