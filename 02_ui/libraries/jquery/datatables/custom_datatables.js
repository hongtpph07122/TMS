$.fn.dataTable.ext.errMode              = 'none';
$.fn.dataTable.ext.pager.numbers_length = 6;
var iTables                             = [];

function loadDataTable( variable_options ) {
    var options = {};
    if ( variable_options ) options = variable_options;
    if ( options.idx === undefined || options.idx === null || options.idx === '' ) {
        options.idx = 0;
    }
    if ( options.isDynamicCols === undefined || options.isDynamicCols === null || options.isDynamicCols === '' ) {
        options.isDynamicCols = true;
    }
    if ( options.availableCols === undefined || options.availableCols === null || options.availableCols === '' ) {
        options.availableCols = 0;
    }

    var target = '#tblMailList' + options.idx;
    var jumToPage = '#jumpToPage' + options.idx;
    if ( options.isDynamicCols ) {
        if ( $( target ).find( 'thead tr' ).first().find( 'th' ).length <= options.availableCols ) {
            $( target ).find( 'thead' ).html( '<tr><th>' + app.vtranslate( 'No data!' ) + '</th></tr>' );
            return true;
        }
    }
    if ( $( target ).find( '.bhs-filter' ).find( 'input' ).length === 0 ) {
        $( target ).find( '.bhs-filter' ).hide();
    }

    if (app.getViewName() === 'List' && app.getModuleName() !== 'BhsOrderProcessing') {
        options.stateSave = true;
        var idx = options.idx;
        options.sDom = "<'row'<'col-sm-6'><'col-sm-6'f>><'row'<'col-sm-12'tr>><'row'<'col-sm-5'i><'col-sm-7'l<'" + jumToPage + "'>p>>";
        options.initComplete = function(settings, json) {
            $(jumToPage).addClass('input-page-container');
            $(jumToPage).append('<label>' + app.vtranslate("LBL_GO_TO_PAGE") +':</label><input class="input-page" data-idx=' + idx + ' type="number" min="1">');
            $(jumToPage).parent().css({
                "display": "flex",
                "justify-content": "flex-end"
            });
            var currentPage = $(target + '_wrapper').find('.paginate_button.active a').text();
            $(jumToPage).find('.input-page').val(currentPage);

            if ($('div.dataTables_length select').length) {
                $('div.dataTables_length select').addClass('text-inverse');    
            }
        };
    }

    var sAjaxSource = $( target ).attr( "data-url" );
    var defaults    = {
        orderCellsTop: true,
        autoWidth: false,
        "scrollX": false,
        'responsive': true,
        "bInfo": true,
        "searching": true,
        // 'sDom': "<'row'<'col-sm-12'tr>><'row'<'col-sm-5'i><'col-sm-7'p>>",
        "processing": false,
        "serverSide": true,
        "stateSave": false,
        "paging": true,
        "pageLength": 10,
        "lengthChange": false,
        "pagingType": "full_numbers",
        "ajax": {
            url: sAjaxSource, // json datasource
            type: "post",  // method  , by default get
            // dataSrc: "result.data",  // method  , by default get
            data: function ( d ) {
                // Add dynamic parameters to the data object sent to the server
                if ( options.dt_params ) {
                    $.extend( d, options.dt_params );
                }
            },
            error: function ( err ) {  // error handling
                $( target ).find( 'tbody' ).html( '<tr><th colspan="3">No data found in the server</th></tr>' );
            },
        },
        "oLanguage": {
            "oPaginate": {
                "sFirst": "<i class='fa fa-step-backward'></i>",
                "sPrevious": "<i class='fa fa-caret-left'></i>",
                "sNext": "<i class='fa fa-caret-right'></i>",
                "sLast": "<i class='fa fa-step-forward'></i>"
            },
            "sInfo": app.vtranslate("sInfo"),
        },
        'createdRow': function ( row, data, dataIndex ) {
            // $(row).attr('data-id', data[$(row).find('td').length]);
            $( row ).attr( 'data-id', data['bhs_record_id'] );
            // Perform colspan, rowspan
            var spanDefs = data.bhsSpanDefs;
            if ( spanDefs ) {
                var colIdx = spanDefs[0];
                if ( !colIdx ) colIdx = 0;
                var colspan = spanDefs[1];
                if ( !colspan ) colspan = 1;
                var rowspan = spanDefs[2];
                if ( !rowspan ) rowspan = 0;
                $( $( row ).find( 'td' )[colIdx] ).attr( 'colspan', colspan );
                $( $( row ).find( 'td' )[colIdx] ).attr( 'rowspan', rowspan );
                while ( colspan > 1 ) {
                    $( $( row ).find( 'td' )[$( row ).find( 'td' ).length - 1] ).remove();
                    colspan -= 1;
                }
            }
            // Cell color
            var bhsCellClass = data.bhsCellClass;
            if ( bhsCellClass ) {
                for ( var key in bhsCellClass ) {
                    var idxs = bhsCellClass[key];
                    for ( var i = 0; i < idxs.length; i++ ) {
                        $( $( row ).find( 'td' )[idxs[i]] ).addClass( key );
                    }
                }
            }
            // Row color
            var bhsRowClass = data.bhsRowClass;
            if ( bhsRowClass ) {
                $( row ).addClass( bhsRowClass );
            }
        },
        "preDrawCallback": function ( settings ) {
            app.helper.showProgress();
        },
        "drawCallback": function ( settings ) {
            var selectedIds = $( target ).attr( 'data-selectedIds' );
            if ( selectedIds ) {
                selectedIds = selectedIds.split( ',' );
                for ( var i = 0; i < selectedIds.length; i++ ) {
                    $( target + ' tbody tr[data-id="' + selectedIds[i] + '"] input.check-item' ).prop( 'checked', true );
                }
            }
            app.helper.hideProgress();
            if ( typeof options.customDrawCallback == 'function' ) {
                options.customDrawCallback( settings );
            }
            $( '.btn-custom.text-tr-bold' ).closest( 'tr' ).css( 'font-weight', 'bold' );

            if (app.getViewName() === 'List' && app.getModuleName() !== 'BhsOrderProcessing') {
                var currentPage = $(target + '_wrapper').find('.paginate_button.active a').text();
                $(jumToPage).find('.input-page').val(currentPage);
            }
        }
    };
    var columnDefs  = [];
    try {
        columnDefs = JSON.parse( $( target ).attr( 'data-columnDefs' ) );
    } catch ( e ) {
        columnDefs = [];
    }
    if ( columnDefs ) {
        defaults['columnDefs'] = columnDefs;
    }
    options              = $.extend( defaults, options );
    iTables[options.idx] = $( target ).DataTable( options );
    $( target ).on( 'error.dt', function ( e, settings, techNote, message ) {
        console.error( 'An error has been reported by DataTables("' + target + '"): ', message );
    } );

    // new $.fn.dataTable.FixedHeader( iTables[options.idx] );
    if ( options.multitable ) {
        $( 'table#tblMailList' + options.idx + ' tr.bhs-filter th input, tr.bhs-filter th select' ).on( 'blur keyup change', function ( e ) {
            if ( $( this ).is( '.dateField' ) || $( this ).is( 'select' ) || e.type === 'blur' || e.keyCode === 13 ) {
                var nodes = Array.prototype.slice.call( $( this ).closest( 'tr' )[0].children );
                var i     = nodes.indexOf( $( this ).closest( 'th' )[0] );
                if ( iTables[options.idx].column( i ).search() !== this.value ) {
                    iTables[options.idx].column( i ).search( this.value ).draw();
                }
            }
            ;
        } );
    } else {
        $( 'table tr.bhs-filter th input, tr.bhs-filter th select' ).on( 'blur keyup change', function ( e ) {
            var parentElm = $( this ).closest( 'th' );
            // validate from || to
            if ( $( this ).is( '.fromField' ) || $( this ).is( '.toField' ) ) {
                var fromElm = parentElm.find( '.fromField' );
                var toElm   = parentElm.find( '.toField' );
                var fromVal = isNaN( parseInt( fromElm.val() ) ) ? 0 : parseInt( fromElm.val() );
                var toVal   = isNaN( parseInt( toElm.val() ) ) ? 0 : parseInt( toElm.val() );
                if ( $( this ).is( '.fromField' ) && (fromVal > toVal) ) {
                    toElm.val( fromVal );
                }
                if ( $( this ).is( '.toField' ) && (fromVal > toVal) ) {
                    fromElm.val( toVal );
                }
                fromElm.attr( 'max', toElm.val() );
                toElm.attr( 'min', fromElm.val() );
            }
            if ( $( this ).is( '.dateField' ) || $( this ).is( 'select' ) || e.type === 'blur' || e.keyCode === 13 ) {
                var value = this.value;
                var nodes = Array.prototype.slice.call( $( this ).closest( 'tr' )[0].children );
                var i     = nodes.indexOf( $( this ).closest( 'th' )[0] );
                if ( iTables[options.idx].column( i ).search() !== this.value ) {
                    if ( $( this ).is( '.fromField' ) || $( this ).is( '.toField' ) ) {
                        var fromValue = parentElm.find( '.fromField' ).val();
                        var toValue   = parentElm.find( '.toField' ).val();
                        value         = fromValue + ',' + toValue;
                    }

                    iTables[options.idx].column( i ).search( value ).draw();

                }
            }
            ;
        } );
    }

    $( '.daterange' ).on( 'apply.daterangepicker', function ( ev, picker ) {
        var nodes = Array.prototype.slice.call( $( this ).closest( 'tr' )[0].children );
        var i     = nodes.indexOf( $( this ).closest( 'th' )[0] );
        if ( iTables[options.idx].column( i ).search() !== this.value ) {
            iTables[options.idx].column( i ).search( this.value ).draw();
        }
    } );


}

function calculateSelectedId( selector ) {
    var parent      = $( selector ).closest( 'table' );
    var checked     = $( selector ).prop( 'checked' );
    var id          = $( selector ).attr( 'data-id' );
    var selectedIds = parent.attr( 'data-selectedids' );
    if ( !selectedIds ) {
        selectedIds = [];
    } else {
        selectedIds = selectedIds.split( ',' );
    }
    var idx = selectedIds.indexOf( id );
    if ( idx > -1 ) {
        if ( !checked ) {
            selectedIds.splice( idx, 1 );
        }
    } else {
        if ( checked ) {
            selectedIds.push( id );
        }
    }
    var total   = selectedIds.length;
    selectedIds = selectedIds.join( ',' );
    var msg     = '';
    if ( total == 1 ) {
        msg = app.vtranslate( '1 record is selected.' );
    } else if ( total > 1 ) {
        msg = '$_NUMBER_ROCORDS_$ records are selected.';
        msg = msg.replace( '$_NUMBER_ROCORDS_$', total );
    }
    if ( msg ) {
        msg = '<p>' + msg + '</p>';
    }
    parent.closest( '.dataTables_wrapper' ).find( '.selectedRecordNumbers' ).html( msg );
    parent.attr( 'data-selectedids', selectedIds );
}

function reloadDataTable( idx ) {
    if ( !idx ) {
        idx = 0;
    }
    iTables[idx].ajax.reload();
}

function reloadDataTableWithUrl( idx, url ) {
    if ( !idx ) {
        idx = 0;
    }
    iTables[idx].ajax.url( url ).load();
}

$( document ).ready( function () {
    var acceptedModule = [ 'BhsCallbackList',
        'BhsCallbackManagement',
        'BhsCampaignManagement',
        'BhsOrders',
        'BhsValidation',
        'BhsShipping',
        'BhsShippingPending',
        'BhsCdrs',
        'BhsCustomer',
        'BhsAgentMonitoring' ];
    if ( iTables.length > 0 && acceptedModule.indexOf( app.getModuleName() ) >= 0 ) {
        $( '<div id ="appnav-refresh" class="pull-right"><button class="btn-refresh btn module-buttons btn-bhs"><i class="fa fa-refresh" aria-hidden="true"></i> Refresh</button></div>' ).appendTo( '.module-action-content .col-lg-7' );
    }

    $('body').on( 'init.dt', function ( e, settings ) {
        var moduleHaveFilter = [
            'BhsCdrs',
            'BhsCampaignManagement',
            'BhsOrders',
            'BhsValidation',
            'BhsShipping',
            'BhsShippingPending',
            'BhsCallbackList',
            'BhsCallbackManagement',
            'BhsProduct',
            'BhsCombo',
        ];

        if (app.getViewName() === 'List' && moduleHaveFilter.indexOf( app.getModuleName() ) >= 0 ) {
            var api = new $.fn.dataTable.Api( settings );
            var state = api.state.loaded();
            if (state) {
                var columns = state.columns;
                var filterCount = 0;
                for (var i in columns) {
                    var searchValue = columns[i].search.search;
                    if (searchValue) {
                        var filter = $('tr.bhs-filter th').eq(i).find('span').children();
                        filter.val(searchValue);
                        filterCount++;
                    }
                }
                if (filterCount > 0) {
                    $( ".btn-filter-header.filter" ).trigger('click');
                }
            }
        }
    });

    $( 'body' ).on( 'click', '.check-all', function ( e ) {
        var checked = $( this ).prop( 'checked' );
        var parent  = $( this ).closest( 'table' );
        parent.find( 'input.check-item' ).prop( 'checked', checked );
        // get selected ids
        var checkItems = $( this ).closest( 'table' ).find( 'input.check-item' );
        for ( var i = 0; i < checkItems.length; i++ ) {
            calculateSelectedId( checkItems[i] );
        }
    } );

    $( 'body' ).on( 'click', '.check-item', function ( e ) {
        var parent   = $( this ).closest( 'table' );
        var unchecks = parent.find( 'input.check-item:not(:checked)' ).length;
        var checked  = true;
        if ( unchecks ) {
            checked = false;
        }
        parent.find( 'input.check-all' ).prop( 'checked', checked );
        // get selected ids
        calculateSelectedId( this );
    } );
    $( 'body' ).on( 'click', '.btn-refresh', function ( e ) {
        for ( var i = 0; i < iTables.length; i++ ) {
            iTables[i].ajax.reload();
        }
    } );

    $( '.dataTables_scrollBody' ).on( 'show.bs.dropdown', function () {
        $( '.dataTables_scrollBody' ).css( "overflow", "inherit" );
    } );

    $( '.dataTables_scrollBody' ).on( 'hide.bs.dropdown', function () {
        $( '.dataTables_scrollBody' ).css( "overflow", "auto" );
    } );
    $( '.bhs-filter' ).hide();
    $( ".btn-filter-header.filter" ).on( 'click', function () {
        var boolean   = $( '.bhs-filter' ).is( ":visible" ),
            open_txt  = $( this ).data( 'open' ),
            close_txt = $( this ).data( 'close' );

        if ( boolean ) {
            $( this ).find( 'i' ).html( 'filter_list' );
            $( this ).find( 'span' ).html( open_txt );
            $( '.btn-clear-filter' ).hide();
            $( '.bhs-filter' ).hide();
        } else {
            $( this ).find( 'i' ).html( 'clear' );
            $( this ).find( 'span' ).html( close_txt );
            $( '.btn-clear-filter' ).show();
            $( '.bhs-filter' ).show();
        }
    } );
    $( ".filter" ).not( ".btn-filter-header" ).on( 'click', function () {
        $( 'tr.bhs-filter th input, tr.bhs-filter ' ).val( '' );
        $( 'th select' ).val( '' );
        $( 'th select' ).val( '' ).trigger( 'change' );
        $( "#tblMailList0,#tblMailList1" ).DataTable().search( '' ).columns().search( '' ).draw();
    } );
    $( ".btn-filter-header" ).on( 'click', function () {
        if ( $( this ).hasClass( 'pre' ) ) {
            $( '.paginate_button.previous' ).click();
        }
        if ( $( this ).hasClass( 'next' ) ) {
            $( '.paginate_button.next' ).click();
        }
    } );

    $("body").on("change", ".input-page", function(e) {
        var jumpPage = parseInt($(this).val());
        var idx = $(this).data('idx');
        iTables[idx].page(jumpPage - 1).draw(false);
    });
} );
