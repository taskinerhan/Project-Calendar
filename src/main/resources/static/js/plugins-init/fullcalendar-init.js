!function(e) {
    "use strict";
    var t = function() {
        this.$body = e("body"),
        this.$modal = e("#event-modal"),
        this.$event = "#external-events div.external-event",
        this.$calendar = e("#calendar"),
        this.$saveCategoryBtn = e(".save-category"),
        this.$categoryForm = e("#add-category form"),
        this.$extEvents = e("#externasl-events"),
        this.$calendarObj = null
    };

    t.prototype.onDrop = function(t, n) {
        var a = t.data("eventObject"),
            o = t.attr("data-class"),
            i = e.extend({}, a);
        i.start = n, o && (i.className = [o]), this.$calendar.fullCalendar("renderEvent", i, !0), e("#drop-remove").is(":checked") && t.remove()
    };

    t.prototype.onEventClick = function(t, n, a) {
        var o = this,
            i = e("<form></form>");
        i.append("<label>Change event name</label>"),
        i.append("<div class='input-group'><input class='form-control' type='text' value='" + t.title + "' /><span class='input-group-btn'><button type='submit' class='btn btn-success waves-effect waves-light'><i class='fa fa-check'></i> Save</button></span></div>"),
        o.$modal.modal({ backdrop: "static" }),
        o.$modal.find(".delete-event").show().end().find(".save-event").hide().end().find(".modal-body").empty().prepend(i).end().find(".delete-event").unbind("click").on("click", function() {
            o.$calendarObj.fullCalendar("removeEvents", function(e) {
                return e._id == t._id
            }), o.$modal.modal("hide")
        }),
        o.$modal.find("form").on("submit", function() {
            return t.title = i.find("input[type=text]").val(), o.$calendarObj.fullCalendar("updateEvent", t), o.$modal.modal("hide"), !1
        })
    };

    t.prototype.onSelect = function(start, end) {
        var o = this;

        var day = start.format("DD");
        var month = start.format("MM");
        var year = start.format("YYYY");

        var itemList = [];

        $.ajax({
            url: '/ajax/' + day + '/' + month + '/' + year,
            type: 'POST',
            dataType: 'json',
            success: function(response) {
                itemList = response;
                console.log(itemList);
                $('#item-list').empty();
                if (itemList.length > 0) {
                    itemList.forEach(function(item) {
                        $('#item-list').append(`
                            <div class="card mb-2">
                                <div class="card-body">
                                    <h5 class="card-title">${item.product}</h5>
                                    <p class="card-text">Price: $${item.price}</p>
                                    <p class="card-text">Expense Item: ${item.expenseItem}</p>
                                </div>
                            </div>
                        `);
                    });
                } else {
                    $('#item-list').append('<div class="alert alert-warning">No items found for this date.</div>');
                }
                $('#event-modal').modal('show');
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
            }
        });

        o.$calendarObj.fullCalendar("unselect");
    };

    t.prototype.enableDrag = function() {
        e(this.$event).each(function() {
            var t = {
                title: e.trim(e(this).text())
            };
            e(this).data("eventObject", t),
            e(this).draggable({
                zIndex: 999,
                revert: !0,
                revertDuration: 0
            });
        });
    };

    t.prototype.changeButtonColor = function(start, end) {
        var day = start.format("DD");
        var month = start.format("MM");
        var year = start.format("YYYY");
        var self = this;

    };

    t.prototype.init = function() {
        this.enableDrag();
        var o = this;

        o.$calendarObj = o.$calendar.fullCalendar({
            slotDuration: "00:15:00",
            minTime: "08:00:00",
            maxTime: "19:00:00",
            defaultView: "month",
            handleWindowResize: !0,
            height: e(window).height() - 200,
            header: {
                left: "prev,next today",
                center: "title",
                right: "month,agendaWeek,agendaDay"
            },
            editable: !0,
            droppable: !0,
            eventLimit: !0,
            selectable: !0,
          dayRender: async function(date, cell) {
              var day = date.date();
              var year = date.year();
              var month = date.month() + 1;
              var value = 0;
              try {
                  let response = await $.ajax({
                      url: 'ajax/colors/' + day + '/' + month + '/' + year,
                      method: 'GET'
                  });

                  value = response;
                  console.log("value: " + value + ":-" + " date: " + day, month, year)


                  if (value != 0) {
                      $(cell).css('background-color', '#caefca');
                  }
                  value = 0;

              } catch (error) {
                  console.log('error.');
              }
          },


            drop: function(t) {
                o.onDrop(e(this), t);
            },
            select: function(e, t, n) {
                o.onSelect(e, t, n);
            },
            eventClick: function(e, t, n) {
                o.onEventClick(e, t, n);
            }
        }),

        this.$saveCategoryBtn.on("click", function() {
            var e = o.$categoryForm.find("input[name='category-name']").val(),
                t = o.$categoryForm.find("select[name='category-color']").val();
            null !== e && 0 != e.length && (o.$extEvents.append('<div class="external-event bg-' + t + '" data-class="bg-' + t + '" style="position: relative;"><i class="fa fa-move"></i>' + e + "</div>"), o.enableDrag());
        });
    };

    e.CalendarApp = new t,
    e.CalendarApp.Constructor = t
}(window.jQuery),

function(e) {
    "use strict";
    e.CalendarApp.init();
}(window.jQuery);
