<div  style="margin: 0px 30px;">
	<h4>{vtranslate('LBL_BhsAgentReport', $MODULE)}</h4>
	<ul class="list-unstyled">
		<li >
			<a><b>1.</b>&nbsp;{vtranslate('LBL_AGENT_CALL_REPORT', $MODULE)}</a>
			<ul >
				<li type="a"><a href="index.php?module={$MODULE}&view=NumberCallsByType">&nbsp;{vtranslate('LBL_NUMBER_OF_CALLS_BY_CALL_TYPE_TABLE', $MODULE)}</a></li>
			</ul>
		</li>
		<li>
			<a><b>2.</b>&nbsp;{vtranslate('LBL_AGENT_DURATION_REPORT', $MODULE)}</a>
			<ul >
				<li type="a"><a href="index.php?module={$MODULE}&view=DurationByCallType">&nbsp;{vtranslate('LBL_CALL_DURATION_BY_CALL_TYPE_TABLE', $MODULE)}</a></li>
			</ul>
		</li>
		<li>
			<a><b>3.</b>&nbsp;{vtranslate('LBL_AGENT_CALL_DETAIL_REPORT', $MODULE)}</a>
			<ul >
				<li type="a"><a href="index.php?module={$MODULE}&view=CallDetail">&nbsp;{vtranslate('LBL_CALL_DETAIL_TABLE', $MODULE)}</a></li>
			</ul>
		</li>
		<li>
			<a><b>4.</b>&nbsp;{vtranslate('LBL_AGENT_ACTIVITY_REPORT', $MODULE)}</a>
			<ul >
				<li type="a"><a href="index.php?module={$MODULE}&view=AgentActivityCounts">&nbsp;{vtranslate('LBL_AGENT_ACTIVITY_COUNTS_TABLE', $MODULE)}</a></li>
				<li type="a"><a href="index.php?module={$MODULE}&view=ActivityDuration">&nbsp;{vtranslate('LBL_ACTIVITY_DURATION_TABLE', $MODULE)}</a></li>
				<li type="a"><a href="index.php?module={$MODULE}&view=AgentLoginTime">&nbsp;{vtranslate('LBL_AGENT_LOGIN_TIME', $MODULE)}</a></li>
			</ul>
		</li>
		<li>
			<a><b>5.</b>&nbsp;{vtranslate('LBL_AGENT_SUMMARY_REPORT', $MODULE)}</a>
			<ul >
				<li type="a"><a href="index.php?module={$MODULE}&view=ActivitySummary">&nbsp;{vtranslate('LBL_ACTIVITY_SUMMARY_TABLE', $MODULE)}</a></li>
			</ul>
		</li>
	</ul>
</div>