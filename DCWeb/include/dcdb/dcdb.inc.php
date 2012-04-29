<?

class Dcdb
{
	var $user = 'USERNAME';
	var $pass = 'PASS';
	var $host = 'DBHOST';
	var $dbName = 'DBNAME';
	var $link;
	
	function Dcdb() {
		$this->link = mysql_connect($this->host, $this->user, $this->pass);
		mysql_select_db ($this->dbName);
	}
	
    function query($sql) {
		$result = mysql_query($sql, $this->link);
		return $result;
    }
}


?>