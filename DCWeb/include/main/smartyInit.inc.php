<?
function smartyInit()
{
	require('include/smarty/Smarty.class.php');
	$smarty = new Smarty();
	$smarty->template_dir = 'smarty/templates';
	$smarty->compile_dir = 'smarty/templates_c';
	$smarty->cache_dir = 'smarty/cache';
	$smarty->config_dir = 'smarty/configs';
	return $smarty;
}

?>