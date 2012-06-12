Lang = {
    isFunction: function( obj ) {
      return toString.call(obj) === "[object Function]";
    },
    isArray: function( obj ) {
        return toString.call(obj) === "[object Array]";
    },
    isObject: function(obj){ 
      return typeof(obj)==='object' && !this.isFunction(obj) && !this.isArray(obj);
    },
    isBoolean: function(obj){ 
      return  typeof(obj)==='boolean';
    },
    isString: function(obj){ 
      return  typeof(obj)==='string';
    },
    isNull: function(obj){ 
      return  obj === null;
    },
    isUndefined: function(obj){ 
      return  obj === undefined;
    },
    isNumber: function(obj){ 
      return  $.isNumeric(obj);
    },
};
DVIZ = {
    doDataTables: true,
    doDates: true,
    doPreserve: true,
    doTruncate: true,
    
    paths: [],
    /** Returns a string as it would need to be encoded if it were going to
     * be used to access the property of an object in javascript.
     * This will add either a dot for dot-notation, or use square brackets
     * if necessary.
     * @param  {string} s
     / @return {string}
     */
   variable: function (s) {
     if (
       this.variableRE.test(s) === true &&
         this.reserved.indexOf(s) === -1
     ) {
       // No need to HTML encode this.
       // Characters that are not legal HTML are also not legal in
       // javascript variables, and would have been detected by the
       // regular expression.
       return '.' + s;
     }

     return '[<span class="STRING">' +
       this.html(JSON.stringify(s)) + '</span>]';
   },
   /** Prepare a string for insertion as HTML.
    * @param  {string} s
    * @return {string}
    */
  html: function (s) {
    return s
      .toString()
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;');
  },

  /** This is a list of reserved words in javascript. They cannot be valid
   * variable names, so when we render a path, these words cannot be used
   * with dot-notation.
   * @property
   * @type {array}
   */
 reserved: [
   'abstract', 'boolean', 'break', 'byte', 'case', 'catch', 'char',
   'class', 'const', 'continue', 'debugger', 'default', 'delete', 'do',
   'double', 'else', 'enum', 'export', 'extends', 'false', 'final',
   'finally', 'float', 'for', 'function', 'goto', 'if', 'implements',
   'import', 'in', 'instanceof', 'int', 'interface', 'long', 'native',
   'new', 'null', 'package', 'private', 'protected', 'public', 'return',
   'short', 'static', 'super', 'switch', 'synchronized', 'this', 'throw',
   'throws', 'transient', 'true', 'try', 'typeof', 'var', 'void',
   'volatile', 'while', 'with'
 ],
  /** This regular expresion helps identify whether a string could be a
   * legal javascript variable name.  Javascript variables must start with
   * a letter, underscore, or $ symbol. Subsequent characters may also
   * include numbers.
   * @property
   * @type {RegExp}
   */
  variableRE: /^[a-z_$][\w$]*$/i,

  /** Static regular expression, used to detect dates.
   * @property
   * @type {RegExp}
   */
  dateRE: /^(\d{4})-(\d\d)-(\d\d)T(\d\d):(\d\d):(\d\d)Z$/,

  /** Format a number to be human-readable, with commas and whatnot.
  *
  * @param  {number} n
  * @return {string}
  */
  formatSize: function (n) {
    if (isNaN(n) || n.length === 0) {
      return '';
    }
  
    var s = n.toString(),
      i = s.length - 3;
  
    while (i >= 1) {
      s = s.substring(0, i) + ',' + s.substring(i, s.length);
      i -= 3;
    }
    return s;
  },
};

/** Provides a few necessary methods for detecting and rendering a generic
  * data structure.
  * @constructor
  * @param {object | array} o
  */
STRUCTURE = function (o) {
  this.footPrints = [];
  this.length = 0;

  // Determine the mode of all subobject property sets.  This also updates
  // the length property.
  this.footPrint = this.scanObject(o);
};


/** A single foot print.  Used to help determine which set of properties
  * occurs the most often in a set of sets.
  *
  * @constructor
  * @param {array} keys
  */
STRUCTURE.Footprint = function (keys) {
  this.keys = keys.slice();
  this.count = 1;
};


STRUCTURE.Footprint.prototype = {

  /** Determine if this footprint is equal to another set of keys.
    * @param  {array} keys
    * @return {boolean}
    */
  equals: function (keys) {
    var x;
    if (this.keys.length === keys.length) {
      for (x = 0; x < keys.length; x++) {
        if (this.keys[x] !== keys[x]) {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  },

  /** Render this footprint as a set of HTML table cells.
    * @return {string}
    */
  render: function (row, path) {
    var keys   = this.keys,
      x      = 0,
      k      = keys.length,
      output = [];

    if (row) {
      for (; x < k; x++) {
        DVIZ.paths.push(DVIZ.paths[path] + DVIZ.variable(keys[x]));

        output[x] =
          '<td id="px' + (DVIZ.paths.length - 1) + '">' +
          HTML.display(row[keys[x]], DVIZ.paths.length - 1) +
          '<\/td>';
      }
    } else {
      for (; x < k; x++) {
        output[x] = '<th>' + DVIZ.html(keys[x]) + '<\/th>';
      }
    }

    return output.join('');
  }
};



/** Determine whether a given object is valid for inclusion as a row in a
  * datatable structure.
  * @param  {object} o
  * @return {boolean}
  */
STRUCTURE.isObject = function (o) {
  return (
    Lang.isObject(o)    === true  &&
    Lang.isArray(o)     === false &&
    Lang.isFunction(o)  === false &&
    o instanceof Error  === false &&
    o instanceof Date   === false &&
    o instanceof RegExp === false
  );
};


/** Sort an array numerically */
STRUCTURE.numericSort = function (a, b) {
  return a - b;
};


/** Extract a sorted list of keys from the given object.
  * @param  {object} o
  * @return a
  */
STRUCTURE.getKeys = function (o) {
  var a = [], y;

  for (y in o) {
    if (o.hasOwnProperty(y)) {
      a[a.length] = y;
    }
  }

  if (Lang.isArray(o)) {
    a.sort(STRUCTURE.numericSort);
  } else {
    a.sort();
  }

  return a;
};


STRUCTURE.prototype = {

  /** Scan an object for inclusion in this data structure.
    *
    * Updates the length property of this structure.
    *
    * @param  {object} o
    * @return {array}
    *
    */
  scanObject: function (o) {
    var x, keys, length = 0;
    for (x in o) {
      if (o.hasOwnProperty(x)) {
        length++;
        if (STRUCTURE.isObject(o[x])) {
          keys = STRUCTURE.getKeys(o[x]);
          if (keys.length > 0) {
            this.addFootPrint(keys);
          }
        }
      }
    }

    this.length += length;
    return this.getMode();
  },


  /** Add a single footprint to our collection of footprints.
    * @param {array} keys
    */
  addFootPrint: function (keys) {
    var x, footPrints = this.footPrints;

    for (x = 0; x < footPrints.length; x++) {
      if (footPrints[x].equals(keys)) {
        footPrints[x].count++;
        return true;
      }
    }

    footPrints[footPrints.length] = new STRUCTURE.Footprint(keys);
    return true;
  },

  /** Return the mode of the footprints (i.e., the one that occurred the
    * most often).
    *
    * @return {STRUCTURE.FootPrint}
    */
  getMode: function () {
    var x,
      count      = 0,
      max        = null,
      footPrints = this.footPrints;

    for (x = 0; x < footPrints.length; x++) {
      if (footPrints[x].count > count) {
        max = footPrints[x];
        count = max.count;
      }
    }
    return max;
  },

  /** This is only valid as a structure if at least two items have been
    * scanned, and if the the mode of all subobject property sets occurred
    * at least X% of the time.
    *
    * @param  {number}  threshold
    * @return {boolean}
    */
  isValid: function (threshold) {
    return (
      this.length > 1 &&
      this.footPrint !== null &&
      this.footPrint.count >= (this.length * threshold)
    );
  },


  /** Render the given object using this data structure as a framework.
    * @param {object | array} obj
    * @param {number}         path
    * @return {string}
    */
  render: function (obj, path) {
    var x, keys, row, subPath,
      properties = STRUCTURE.getKeys(obj),
      span       = this.footPrint.keys.length,
      o          = [],
      p          = 0,
      isArray    = Lang.isArray(obj);

    o[p++] =
      '<table id="p' + path + '" class="ARRAY">' +
      '<caption>[-] ' +
      (isArray ? 'Array' : 'Object') +
      ' data structure, ' + DVIZ.formatSize(properties.length) +
      (isArray ? ' items' : ' properties') +
      '</caption><thead><tr><th>[key]</th>';

    o[p++] = this.footPrint.render();
    o[p++] = '</tr></thead><tbody>';

    path = DVIZ.paths[path];

    for (x = 0; x < properties.length; x++) {
      row = obj[properties[x]];
      keys = STRUCTURE.getKeys(row);

      subPath = 
        (isArray && isNaN(parseInt(properties[x], 10)) === false) ?
        '[<span class="NUMBER">' + properties[x] + '</span>]' :
        DVIZ.variable(properties[x]);

      DVIZ.paths.push(path + subPath);

      if (this.footPrint.equals(keys)) {
        o[p++] =
          '<tr id="p' + (DVIZ.paths.length - 1) + '"><th' +
          (isArray ? ' class="NUMBER">' : '>') +
          DVIZ.html(properties[x]) + '</th>';

        o[p++] = this.footPrint.render(row, DVIZ.paths.length - 1);
        o[p++] = '</tr>';

      } else {
        o[p++] =
          '<tr id="p' + (DVIZ.paths.length - 1) + '"><th><em' +
          (isArray ? ' class="NUMBER">' : '>') +
          DVIZ.html(properties[x]) + '</em></th><td colspan="' +
          span + '">';

        o[p++] = HTML.display(row, DVIZ.paths.length - 1);
        o[p++] = '</td></tr>';
      }
    }

    o[p++] = '</tbody></table>';
    return o.join('');
  }
};

HTML = {

    toggleArea: function (captionNode) {

      var x,
        table = captionNode.parentNode,
        state = captionNode.firstChild.nodeValue.substring(1, 2);

      if (state === '-') {

        if (table.tHead) {
          table.tHead.style.display = 'none';
        }

        for (x = 0; x < table.tBodies.length; x += 1) {
          table.tBodies[x].style.display = 'none';
        }

        captionNode.firstChild.nodeValue =
          '[+]' + captionNode.firstChild.nodeValue.substring(3);

      } else {

        if (table.tHead) {
          table.tHead.style.display = '';
        }

        for (x = 0; x < table.tBodies.length; x += 1) {
          table.tBodies[x].style.display = '';
        }

        captionNode.firstChild.nodeValue =
          '[-]' + captionNode.firstChild.nodeValue.substring(3);

      }

    },

    /** Render a javascript object of unknown type as HTML
      * @param  {object} obj
      * @param  {number} path
      * @return {string}
      */
    display: function (obj, path) {

      return Lang.isArray(obj) ?
        DVIZ.doDataTables ?
        this.structure(obj, path) :
        this.array(obj, path) :

        Lang.isBoolean(obj) ?
        this.bool(obj, path) :

        Lang.isFunction(obj) ?
        this.func(obj, path) :
      
        Lang.isNull(obj) ?
        '<span id="p' + path + '" title="null" class="NULL">null</span>' :

        Lang.isNumber(obj) ?
        '<span id="p' + path + '" title="Number" class="NUMBER">' +
        obj.toString() + '</span>' :

        Lang.isString(obj) ?
        this.string(obj, path) :

        Lang.isUndefined(obj) ?
        '<span id="p' + path +
        '" title="undefined" class="UNDEF">undefined</span>' :

        (obj instanceof Error) ?
        this.err(obj, path) :

        (obj instanceof Date) ?
        this.date(obj, path) :

        (obj instanceof RegExp) ?
        this.regExp(obj, path) :

        Lang.isObject(obj) ?
        DVIZ.doDataTables ?
        this.structure(obj, path) :
        this.obj(obj, path) :


        isNaN(obj) ?
        '<span id="p' + path + '" title="NaN" class="ERR">NaN</span>' :

        (obj === Infinity) ?
        '<span id="p' + path +
        '" title="Infinity" class="ERR">Infinity</span>' :

        '<span id="p' + path + '" class="IDK">[Unknown Data Type]</span>';
    },


    /** Render a javascript array as HTML
      * @param  {array}  a     The array to be rendered
      * @param  {number} path  The path by which this array is reached.
      * @return {string}
      */
    array: function (a, path) {
      var x, body = '';

      for (x = 0; x < a.length; x++) {
        DVIZ.paths.push(
          DVIZ.paths[path] + '[<span class="NUMBER">' + x + '<\/span>]'
        );

        body +=
          '<tr id="p' + (DVIZ.paths.length - 1) + '"><th>' +
          x + '</th><td>' +
          this.display(a[x], DVIZ.paths.length - 1) + '</td></tr>';
      }

      return (body.length) ?
        '<table id="p' + path + '" class="ARRAY">' +
        '<caption>[-] Array, ' + DVIZ.formatSize(a.length) +
        (a.length === 1 ? ' item' : ' items') +
        '</caption><tbody>' + body + '</tbody></table>' :

        '<span id="p' + path +
        '" title="Array" class="ARRAY">[ Empty Array ]</span>';
    },

    /** Render a javascript boolean value as HTML
      * @param  {boolean} b
      * @param  {number}  path
      * @return {string}
      */
    bool: function (b, path) {
      return (b) ?
        '<span id="p' + path +
        '" title="Boolean" class="BOOL">true</span>' :

        '<span id="p' + path +
        '" title="Boolean" class="BOOL">false</span>';
    },

    /** Render a javascript string as HTML
      * @param  {string} s
      * @param  {number} path
      * @return {string}
      */
    string: function (s, path) {
      if (s.length === 0) {
        return '<span id="p' + path +
          '" title="String" class="EMPTY">[zero-length string]</span>';
      }

      // Check and see if this is secretly a date
      if (DVIZ.doDates && DVIZ.dateRE.test(s)) {
        return this.date(Lang.JSON.stringToDate(s), path);
      }

      var tag = DVIZ.doPreserve ? 'pre' : 'span';

      if (DVIZ.doTruncate && s.length > 70) {
        s = s.substring(0, 70) + '\u2026'; // 2026 = "..."
      }

      return '<' + tag + ' id="p' + path +
        '" title="String" class="STRING">' + DVIZ.html(s) +
        '</' + tag + '>';
    },

    /** Render a javascript regular expression as HTML
      * @param  {RegExp} re
      * @param  {number} path
      * @return {string}
      */
    regExp: function (re, path) {
      var output = "/" + DVIZ.html(re.source) + "/";
      if (re.global) {
        output += 'g';
      }
      if (re.ignoreCase) {
        output += 'i';
      }
      if (re.multiline) {
        output += 'm';
      }
      return '<span id="p' + path +
        '" title="RegEx" class="REGEXP">' + output + '</span>';
    },

    /** Render a javascript object as HTML
      * @param  {object} o
      * @param  {number} path
      * @return {string}
      */
    obj: function (o, path) {
      var x, body = [];

      for (x in o) {
        if (o.hasOwnProperty(x)) {
          DVIZ.paths.push(DVIZ.paths[path] + DVIZ.variable(x));

          body.push(
            '<tr id="px' + (DVIZ.paths.length - 1) + '"><th>' +
              DVIZ.html(x) + '</th><td>' +
              this.display(o[x], DVIZ.paths.length - 1) +
              '</td></tr>'
          );
        }
      }

      return (body.length) ?
        '<table id="p' + path + '" class="OBJ">' +
        '<caption>[-] Object, ' + DVIZ.formatSize(body.length) +
        (body.length === 1 ? ' property' : ' properties') +
        '</caption><tbody>' + body.join('') + '</tbody></table>' :

        '<span id="p' + path +
        '" title="Object" class="OBJ">{ Empty Object }</span>';
    },

    /** Render a javascript date object as HTML
      * @param  {Date} d
      * @param  {number} path
      * @return {string}
      */
    date: function (d, path) {
      if (isNaN(d)) {
        return '<span id="p' + path +
          '" title="Date" class="ERR">Invalid Date</span>';
      }

      function pad(num) {
        var s = num.toString();
        return (num < 10) ? '0' + s : s;
      }

      function format(yyyy, mm, dd, hh, nn, ss) {
        var hh12 = (hh === 0) ? 12 : (hh > 12) ? hh - 12 : hh,
          tt = (hh > 11) ? 'PM' : 'AM';

        return (
          yyyy + '-' +
          pad(mm) + '-' +
          pad(dd) + ' ' +
          pad(hh12) + ':' +
          pad(nn) + ':' +
          pad(ss) + ' ' + tt
        );
      }

      var local = format(
        d.getFullYear(),
        d.getMonth() + 1,
        d.getDate(),
        d.getHours(),
        d.getMinutes(),
        d.getSeconds()
      ),

        utc = format(
          d.getUTCFullYear(),
          d.getUTCMonth() + 1,
          d.getUTCDate(),
          d.getUTCHours(),
          d.getUTCMinutes(),
          d.getUTCSeconds()
        ),

        output = utc + ' UTC (' + local + ' Local)';

      return '<span id="p' + path + '" title="Date" class="DATE">' +
        output + '</span>';
    },

    /** Render a javascript error as HTML
      * @param  {Error}  e
      * @param  {number} path
      * @return {string}
      */
    err: function (e, path) {
      if (e.message === 'parseJSON') {
        return '<span id="p' + path +
          '" title="Error" class="ERR">Invalid JSON</span>';
      }

      return '<span id="p' + path + '" title="Error" class="ERR">' +
        DVIZ.html(e.message) + '</span>';
    },

    /** Render a javascript function as HTML
      * @param  {function} f
      * @param  {number}   path
      * @return {string}
      */
    func: function (f, path) {
      var i, s = f.toString();
      if (DVIZ.doTruncate) {
        i = s.indexOf('{') + 50;
        if (i < s.length) {
          s = DVIZ.html(s.substring(0, i)) + '\u2026<br \/>}';
          return '<code id="p' + path +
            '" title="Function (truncated)" class="FUNC">' + s +
            '<\/code>';
        }
      }
      return '<code id="p' + path + '" title="Function" class="FUNC">' +
        DVIZ.html(s) + '<\/code>';
    },

    /** Detect a data table.  I define this as an object (or array) containing
      * two or more items, in which at least 66% of the items are similar to
      * one another. Two objects are similar if they have the same number of
      * key values, and all of the key values of one object are found in the
      * other object.
      * <p>
      * We are going to loop through the properties of the object (or items of
      * the array), and for each object that we find, we are going to collect
      * a list of its keys. Each list will be like a footprint.  We will keep
      * a list of all distinct footprints we find.  Each time we generate a
      * new footprint, we compare it to the others in the list.  If we have
      * seen it before, we make a note of how many times we see it.  If we
      * have not seen it before, we add it to our list of footprints. 
      * When this is done, we will be able to see how many different footprints
      * we have, and whether any one footprint occurs at least 66% of the time.
      * <p>
      * Items that are better represented as other values (dates, numbers,
      * strings, booleans, etc) are counted towards the total length of the
      * structure, but they do not contribute footprints.
      * <p>
      * After we decide which footprint is dominant, we begin generating a
      * tabular structure.  We use the list of keys as the column names (plus
      * one for the row number), then loop over each item in the object.  If
      * that item's footprint matches, it is rendered into the tabular
      * structure.  If it doesn't match, it gets a colspan, and is rendered
      * normally.
      * <p>
      * If it is determined that this is NOT a valid structure, rendering
      * will proceed with either VIS.array or VIS.object, as appropriate.
      *
      * @param  {object | array}  obj
      * @param  {number}          path
      * @return {string}
      */
    structure: function (obj, path) {
      var structure = new STRUCTURE(obj);

      if (structure.isValid(2 / 3) === false) {
        return Lang.isArray(obj) ?
          this.array(obj, path) :
          this.obj(obj, path);
      }

      return structure.render(obj, path);
    }

  };


