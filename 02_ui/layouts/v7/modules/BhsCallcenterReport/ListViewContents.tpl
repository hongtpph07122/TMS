<div  style="margin: 0px 30px;">
	<h4>{vtranslate('Callcenter Report', $MODULE)}</h4>
	<ul class="list-unstyled">
		<li>
			<a><b>1.</b>&nbsp;{vtranslate('LBL_DIALLED_LISTS_REPORTS', $MODULE)}</a>
			<ul class="sub-report">
				<li type="a"><a href="index.php?module={$MODULE}&view=DialledListsDetails">&nbsp;{vtranslate('LBL_DIALLED_LISTS_DETAILS_REPORT', $MODULE)}</a></li>
				<li type="a"><a href="index.php?module={$MODULE}&view=DialledListsSummary">&nbsp;{vtranslate('LBL_DIALLED_LISTS_SUMMARY_REPORT', $MODULE)}</a></li>
			</ul>
		</li>
		<li>
			<a><b>2.</b>&nbsp;{vtranslate('LBL_CALL_CENTER_REPORTS', $MODULE)}</a>
			<ul class="sub-report">
				<li type="a"><a href="index.php?module={$MODULE}&view=CallsByHours">&nbsp;{vtranslate('LBL_REPORT_CALLS_BY_HOURS', $MODULE)}</a></li>
				<li type="a"><a href="index.php?module={$MODULE}&view=InboundEnteredLine">&nbsp;{vtranslate('LBL_REPORT_INBOUND_ENTERED_LINE', $MODULE)}</a></li>
				<li type="a"><a href="index.php?module={$MODULE}&view=CallSummary">&nbsp;{vtranslate('LBL_CALL_SUMMARY_REPORT', $MODULE)}</a></li>
				<li type="a"><a href="index.php?module={$MODULE}&view=CallDuration">&nbsp;{vtranslate('LBL_CALL_DURATION', $MODULE)}</a></li>
				<li type="a"><a href="index.php?module={$MODULE}&view=CallSummaryByDate">&nbsp;{vtranslate('LBL_CALL_SUMMARY_BY_DATE_REPORT', $MODULE)}</a></li>
			</ul>
		</li>
		<li>
			<a><b>3.</b>&nbsp;{vtranslate('LBL_CUSTOMER_REPORTS', $MODULE)}</a>
			<ul class="sub-report">
				<li type="a"><a href="index.php?module={$MODULE}&view=CustomerAdded">&nbsp;{vtranslate('LBL_REPORT_CUSTOMERS_ADDED_BY_EXTENSIONS', $MODULE)}</a></li>
			</ul>
		</li>
	</ul>
</div>