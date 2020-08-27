<?php
class Database{
  
    // specify your own database credentials
    private $host = "localhost";
    private $db_name = "gas_station";
    private $username = "root";
    private $password = "";
    public $conn;
	public $conns;
	public $rrr;
	private $table_name = "payments";
 

    // get the database connection
    public function getConnection(){
  
        $this->conn = null;
  
        try{
            $this->conn = new mysqli($this->host,$this->username, $this->password, $this->db_name);
           // $this->conn->exec("set names utf8");
		//	echo 'Success';
        }catch(Exception $exception){
            echo "Connection error: " . $exception->getMessage();
        }
  
        return $this->conn;
    }
	
	
function report_log($log_msg)
{
    $log_filename = "report_log";
    if (!file_exists($log_filename)) 
    {
        // create directory/folder uploads.
        mkdir($log_filename, 0777, true);
    }
    $log_file_data = $log_filename.'/log_' . date('d-M-Y') . '.log';
    // if you don't add `FILE_APPEND`, the file will be erased each time you add a log
    file_put_contents($log_file_data, $log_msg . "\n", FILE_APPEND);
} 

}



//$test = new Database();
?>