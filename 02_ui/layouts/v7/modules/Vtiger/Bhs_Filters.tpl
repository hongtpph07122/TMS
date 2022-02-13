{strip}
{foreach from=$HEADERS item=HEADER }
<th>
    {if $HEADER['has_filter'] eq 1}
    <span class="input-group">
        {if $HEADER['datatype'] eq 'select'}
        <select class=" marginLeftZero inputElement " name="status" placeholder="Column_name">
            <option value=""> ALL </option>
            {foreach from=$HEADER['datax'] item=datax}
            <option value="{$datax->id}"> {$datax->name}</option>
            {/foreach}
        </select>
        {elseif $HEADER['datatype'] eq 'numberrange'}
            <input type="number" min="0" class="fmarginLeftZero inputElement fromField"
                   placeholder="{vtranslate('Search', $MODULE)} {vtranslate('from', $MODULE)} {vtranslate($HEADER['label'], $MODULE)}"
                   oninput="this.value = this.value.replace(/[^0-9.]/g, ''); this.value = this.value.replace(/(\..*)\./g, '$1');"
                   style="width: 100px;display: inline-block;">
            <div class="input-group-prepend"  style="display: inline-block;">
                <div class="text-center">~</div>
            </div>
            <input type="number" min="0" class="marginLeftZero inputElement toField"
                   placeholder="{vtranslate('Search', $MODULE)} {vtranslate('to', $MODULE)} {vtranslate($HEADER['label'], $MODULE)}"
                   oninput="this.value = this.value.replace(/[^0-9.]/g, ''); this.value = this.value.replace(/(\..*)\./g, '$1');"
                   style="width: 100px;display: inline-block;">
        {else}
        <input name="{$HEADER['name']}" placeholder="{vtranslate('Search' , $MODULE)} {vtranslate($HEADER['label'], $MODULE)} " class="marginLeftZero inputElement {if $HEADER['datatype'] eq 'date'}dateField{/if} {if $HEADER['datatype'] eq 'daterange'}daterange{/if}" autocomplete="off"/>
            {if $HEADER['datatype']|in_array:['date','daterange']}
                <span class="input-group-addon"><i class="fa fa-calendar "></i></span>
            {/if}
        {/if}
    </span>
    {/if}
</th>
{/foreach}
{/strip}