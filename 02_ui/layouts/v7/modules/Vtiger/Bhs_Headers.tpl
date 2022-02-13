{strip}
    {foreach from=$HEADERS item=HEADER}
        <th>{vtranslate($HEADER['label'], $MODULE)}</th>
    {/foreach}
{/strip}