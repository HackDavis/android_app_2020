$(document).ready(function() 
{
    // Hammerjs example from https://gist.github.com/synthecypher/f778e4f5a559268a874e
    const image = document.getElementById('map');

    const mc = new Hammer.Manager(image);

    const pinch = new Hammer.Pinch();
    const pan = new Hammer.Pan();

    pinch.recognizeWith(pan);

    mc.add([pinch, pan]);

    let adjustScale = 0.05;
    let adjustDeltaX = -150;
    let adjustDeltaY = 0;

    let currentScale = null;
    let currentDeltaX = null;
    let currentDeltaY = null;

    // Handles pinch and pan events/transforming at the same time;
    mc.on("pinch pan", function (ev) {

        const transforms = [];

        // Adjusting the current pinch/pan event properties using the previous ones set when they finished touching
        currentScale = adjustScale * ev.scale;
        currentDeltaX = adjustDeltaX + (ev.deltaX / currentScale);
        currentDeltaY = adjustDeltaY + (ev.deltaY / currentScale);

        // Concatinating and applying parameters.
        transforms.push(`scale(${currentScale})`);
        transforms.push(`translate(${currentDeltaX}px,${currentDeltaY}px)`);
        $('#map').css('transform', transforms.join(' '));
    });

    mc.on("panend pinchend", function (ev) {

        // Saving the final transforms for adjustment next time the user interacts.
        adjustScale = currentScale;
        adjustDeltaX = currentDeltaX;
        adjustDeltaY = currentDeltaY;
    });

    $(`div.content.schedule`).show();
    $('div.schedule-events').empty();
    $('div.invalid-code').fadeOut(1);

    // Switch section on navbar click
    $('div.navbar-element').on('click', function() {
        ClickNavbar($(this));
    })

    $('div.mentors-container div.button').on('click', function() {
        cordova.InAppBrowser.open($(this).attr('data-link'), '_blank', 'location=no,zoom=no');
    })

    ClickNavbar($('div.navbar-element.selected'));

    function ClickNavbar(elem)
    {
        /*if (elem.attr('data-content') == "mentors")
        {
            cordova.InAppBrowser.open('https://help.hackdavis.io', '_blank', 
                'location=no,zoom=no');
            return;
        }
        else */if (elem.attr('data-content') == "schedule")
        {
            // Refresh schedule on click on schedule
            loadSchedule();
        }

        $('div.navbar-element').removeClass('selected')
        elem.addClass('selected');
        $('div.content').hide();
        $(`div.content.${elem.attr('data-content')}`).show();
    }

    const storage = window.localStorage;

    ToggleQRPrompts();

    function ToggleQRPrompts()
    {
        if (storage.getItem("hd-qr") != null)
        {
            $('div.qr-uploaded-container').show();
            $('div.upload-qr-container').hide();
            $('img.qr').attr('src', storage.getItem('hd-qr'));
        }
        else
        {
            $('div.qr-uploaded-container').hide();
            $('div.upload-qr-container').show();
        }
    }

    $('div.scan-button.scan').on('click', function() {
        const booking_ref = $('#ref').val().toString().toUpperCase()
        $.post({
            url: `http://server1.hackdavis.io:8002/api/getticket/?booking_ref=${booking_ref}`,
            success: function(data) {
                if (data != "none")
                {
                    $('img.qr').attr('src', data);
                    storage.setItem("hd-qr", data);
                    ToggleQRPrompts();
                }
                else
                {
                    $('div.invalid-code').fadeIn(250);
                    setTimeout(() => {
                        $('div.invalid-code').fadeOut(250);
                    }, 3000);
                }
            }
        })
    })

    $('div.scan-button.retry').on('click', function() {
        storage.removeItem("hd-qr")
        ToggleQRPrompts();
    })

    let currentTime = Date.now();
    let startTime;
    let endTime;
    let timerInterval;
    let timeAdd = 0;

    function formatNum(num)
    {
        num = Math.floor(num);
        return num < 10 ? `0${num}` : num;
    }

    function updateTimer() 
    {
        currentTime += 500; // TODO: acutally fix this

        let timeUntilEnd;

        if (currentTime <= startTime)
        {
            // Event has not started
            $('div.timer-text').text('24:00:00');
            timeUntilEnd = endTime - startTime;
        }
        else if (currentTime > startTime && currentTime < endTime)
        {
            // Event is in progress
            const diff = Math.abs(endTime - currentTime);
            timeUntilEnd = diff;
            $('div.timer-text').text(
                `${formatNum(diff / 1000 / 60 / 60 % 24)}:${formatNum((diff / 1000 / 60) % 60)}:${formatNum((diff / 1000) % 60)}`);
        }
        else
        {
            // Event is finished
            $('div.timer-text').text('00:00:00');
            clearInterval(timerInterval);
            timeUntilEnd = 0;
        }

        const percentage_done = (timeUntilEnd > 0) ? 1 - timeUntilEnd / (endTime - startTime) : 1;
        const circle_percentage = 220 * percentage_done;

        $('svg.progress circle.fill').css('stroke-dashoffset', `${circle_percentage}%`);
        
        
        setTimeout(() => {
            updateTimer();
        }, 1000);
    }

    function loadSchedule()
    {
        $.get({
            url: "https://hackdavis.io/assets/data/schedule.json",
            headers: {"Access-Control-Allow-Origin": "https://hackdavis.io"}
        }, 
        function(data) 
        {
            $('div.schedule-events').empty();
            startTime = Date.parse(data.startTime);
            endTime = Date.parse(data.endTime);

            updateTimer();
            
            data["data"].forEach((event) => {
                if (event.type == "event")
                {
                    const $elem = $(`<div class='schedule-event'></div>`);
                    $elem.append($(`<div class='time'>${event.time}</div>`));
                    const $info = $(`<div class='info'></div>`);
                    $info.append(`<div class='title'>${event.name}</div>`);
                    $info.append(`<div class='description'>${event.description}</div>`)
                    $elem.append($info);
                    $('div.schedule-events').append($elem);
                }
                else if (event.type == "day")
                {
                    const $elem = $(`<div class='schedule-day-title'>${event.name}</div>`);
                    $('div.schedule-events').append($elem);
                }
            });

            setTimeout(() => {
                // Refresh schedule every 30 mins
                loadSchedule();
            }, 1000 * 60 * 30);
        })
    }

    const app = {
        // Application Constructor
        initialize: function() {
            document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
            loadSchedule();
            ToggleQRPrompts();
        },

        // deviceready Event Handler
        //
        // Bind any cordova events here. Common events are:
        // 'pause', 'resume', etc.
        onDeviceReady: function() {
            this.receivedEvent('deviceready');
        },

        // Update DOM on a Received Event
        receivedEvent: function(id) {
            // do stuff on device ready
            setTimeout(() => {
                $('div.loading-screen').fadeOut(200);
            }, 500);
        }
    };

    app.initialize();

})