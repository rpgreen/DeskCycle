<?
	require("include/graph/jpgraph.php");
	require("include/graph/jpgraph_line.php");
	require("include/main/DcWebManager.inc.php");
	require("include/main/DcUnitConverter.inc.php");
	
	$sessId = $_GET['s'];
	if (!$sessId)
	{ 
		die();
	}

	$manager = new DcWebManager();
	$session = $manager->getSession($sessId);
	
	//TODO: make sure session belongs to user
	
	$session = DcUnitConverter::convertSession($session, "km", "h");
	$ydata = $session->velPoints;
	if (count($ydata) > 1)
	{
		// Create the graph. These two calls are always required
		$graph = new Graph(350,180,"auto");	
		$graph->SetScale("textlin");
		// Create the linear plot
		$lineplot=new LinePlot($ydata);
		$lineplot->SetColor("green");
		// Add the plot to the graph
		$graph->Add($lineplot);
		$title = new Text("Session Velocity");
		//$title->SetFont(FF_ARIAL);
		$graph->title = $title;
		$graph->yaxis->SetTitle("Velocity (km/h)",'middle'); 
		$tScale = $session->pointResolution / 1000;	//ms to s
		
		$graph->xaxis->SetTitle("Time",'middle'); 
		$graph->xaxis->SetLabelFormatCallback('timeformat');
		$interval = 1;
		if (count($ydata) >= 3)
			$interval = count($ydata) / 3;	
		$graph->xaxis->SetTextLabelInterval($interval); 
		$graph->xaxis->SetLabelAlign("right"); 
		$graph->xaxis->HideFirstTicklabel();
		// Display the graph
		$graph->Stroke();
	}
	
	function timeformat($input)
	{	//samples to secs to hms
		global $tScale;
		$secs = $input * $tScale;
		$formatted = formatHms(sec2hms($secs));
		return $formatted;
	}
?>