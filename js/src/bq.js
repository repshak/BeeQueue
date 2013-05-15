(function(){
  bq = {};

  function zeroPad (n) { return (+n < 10 ? '0' : '') + n }
  
  bq.dateFormat = function  (d){
    return ""+d.getFullYear()+"-"+zeroPad(d.getMonth()+1)+"-"+zeroPad(d.getDate())
         +" "+zeroPad(d.getHours())+":"+zeroPad(d.getMinutes())+"."+zeroPad(d.getSeconds());  
  }

  
  var format_rules = {
    byType: {
      DATE: function(data,row,header,column){
        return data[column] && bqux.dateFormat(new Date(data[column]));
      }
    },
    defaultRule: function(data,row,header,column){
      return data[column];
    }
  };
  
  bq.getFormatRule = function(type){
    return format_rules.byType[type] || format_rules.defaultRule ;
  };
  
  
  bq.tab=function(data,table){
    function createAllCells(rows, header){    
        for(var col = 0 ; col < header.columns.length ; col++ ){
          rows.append("td")
          .text(
            function(d,i){
              return bq.getFormatRule(data.header.columns[col].type)(d,i,data.header,col);
            }
          );
        }
    }

    var thead_tr = table.append("thead")
    .append("tr").selectAll("th")
    .data(data.header.columns)
    .enter().append("th")
    .text(function(d){return d.name});
    
    var tbody = table.append("tbody");
    var rows = tbody.selectAll("tr").data(data.rows).enter().append("tr");
    createAllCells(rows,data.header);
  };

    
})();
