var ajaxUrl = "ajax/admin/users/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});

function enabled(checbox, id){
    var enabled = checbox.is(':checked');
    $.ajax({
        type: "POST",
        url: ajaxUrl + id,
        data:'enabled=' + enabled,
        success: function(){
            checbox.closest('tr').toggleClass('disable');
            successNoty(enabled ? 'Enabled' : 'Disabled');
        },
        error: function(){
            $(checbox).prop("checked", !enabled)
        }
    });
}