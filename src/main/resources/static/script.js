var me = 'x';

$(document).ready(function() {
    var source = new EventSource('/tictactoe');
    source.onmessage = function(event) {
        $('#log').append($('<div>').text(event.data));
        handleEvent(event);
    };
    source.onerror = function(event) {
        $('#error').append($('<div>').text(event.data));
    };

    $('#send').on('click', function() {
        var cell = $('input:not(:disabled)').filter(function() {
            return this.value.toLowerCase() === me;
        });
        if(cell.length === 1) {
            var matches = cell.attr('id').match(/^cell(\d{1})-(\d{1})$/);
            var url = '/play/' + matches[1] + '/' + matches[2];
            $.ajax(url, {method: 'GET'});
        }
    });

    $('#sync').on('click', function() {
        $.ajax('/sync', {
            method: 'GET',
            success: function(data) {
                $('#log').append($('<div>').text(data));
            }
        });
    });

    $('#reset').on('click', function() {
        $.ajax('/restart', { method: 'GET' });
    });
});

var handleEvent = function(event) {
    var eventData = JSON.parse(event.data);
    switch(eventData.type) {
        case 'move':
            var el = $('#cell'+ eventData.x +'-'+ eventData.y);
            if(el.length) {
                el.val(eventData.player.toLowerCase());
                el.prop('disabled', true);
            }
            break;
        case 'win':
            $('input.cell').each(function() {
                $(this).prop('disabled', true);
            });
            break;
        case 'reset':
            $('input.cell').each(function() {
                $(this).val('').prop('disabled', false);
            });
            break;
    }
};
