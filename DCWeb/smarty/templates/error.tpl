{include file='header.tpl' title='DCWeb - Error'}

{section name=error loop=$errors}
<p class = "error"> 
{$errors[error]}
</p>
{/section}

{include file='footer.tpl'}