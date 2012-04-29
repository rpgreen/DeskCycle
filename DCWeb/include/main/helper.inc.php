<?
	function dump($v)
	{
		echo "<pre>" . print_r($v, true) . "</pre>";
	}
	
	function getTimeDifference( $start, $end )
	{
		$uts['start']      =    strtotime( $start );
		$uts['end']        =    strtotime( $end );
		if( $uts['start']!==-1 && $uts['end']!==-1 )
		{
			if( $uts['end'] >= $uts['start'] )
			{
				$diff    =    $uts['end'] - $uts['start'];
				if( $days=intval((floor($diff/86400))) )
					$diff = $diff % 86400;
				if( $hours=intval((floor($diff/3600))) )
					$diff = $diff % 3600;
				if( $minutes=intval((floor($diff/60))) )
					$diff = $diff % 60;
				$diff    =    intval( $diff );            
				return( array('days'=>$days, 'hours'=>$hours, 'minutes'=>$minutes, 'seconds'=>$diff) );
			}
			else
			{
				trigger_error( "Ending date/time is earlier than the start date/time", E_USER_WARNING );
			}
		}
		else
		{
			trigger_error( "Invalid date/time data detected", E_USER_WARNING );
		}
		return( false );
	}
	
function formatHms($t, $format="1")
{
	$h = $t['hours'];	//str_pad($t['hours'],2,"0", STR_PAD_LEFT);
	$m = str_pad($t['minutes'],2,"0", STR_PAD_LEFT);
	$s = str_pad($t['seconds'],2,"0", STR_PAD_LEFT);
	switch ($format)
	{
		case "1":
		$s = $h . ":" . $m . ":". $s;
		break;
		
		case "2":
		$s = $h . " hours " . $m . " minutes ". $s . " seconds";
		break;
	}
	
	return $s;
}

  function sec2hms ($sec, $padHours = false) 
  {
    // holds formatted string
    $hms = "";
        // there are 3600 seconds in an hour, so if we
    // divide total seconds by 3600 and throw away
    // the remainder, we've got the number of hours
    $hours = intval(intval($sec) / 3600); 
    // add to $hms, with a leading 0 if asked for
    $hms .= ($padHours) 
          ? str_pad($hours, 2, "0", STR_PAD_LEFT). ':'
          : $hours. ':';
    // dividing the total seconds by 60 will give us
    // the number of minutes, but we're interested in 
    // minutes past the hour: to get that, we need to 
    // divide by 60 again and keep the remainder
    $minutes = intval(($sec / 60) % 60); 
    // then add to $hms (with a leading 0 if needed)
    $hms .= str_pad($minutes, 2, "0", STR_PAD_LEFT). ':';
    // seconds are simple - just divide the total
    // seconds by 60 and keep the remainder
    $seconds = intval($sec % 60); 
    // add to $hms, again with a leading 0 if needed
    $hms .= str_pad($seconds, 2, "0", STR_PAD_LEFT);
	$time = array();
	$time['hours'] = $hours;
	$time['minutes'] = $minutes;
	$time['seconds'] = $seconds;
    return $time;
	// done!
	//return $hms;
   }
   
   function daysInMonth($month, $year)
	{
		// calculate number of days in a month
		return $month == 2 ? ($year % 4 ? 28 : ($year % 100 ? 29 : ($year % 400 ? 28 : 29))) : (($month - 1) % 7 % 2 ? 30 : 31);
	}

?>