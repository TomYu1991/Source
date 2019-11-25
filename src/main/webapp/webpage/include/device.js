var device={debug:true};
(function($){

    function createWebSocket(url){
        var ws=null;
        if ('WebSocket' in window) {
            ws = new WebSocket(url);
        } else if ('MozWebSocket' in window) {
            ws = new MozWebSocket(url);
        }
        return ws;
    }
    function  WebSocketClass( ){
        var type= (this.type);
        var position= (this.position);
        var operation= (this.operation);
        var callback=this.callback;
        var isclose=this.isclose;
        var count=this.count!=undefined?this.count:1;
        var interval=this.interval!=undefined?this.interval:1000;
        var data=this.data;
        this.web_senddata=function(){
            var ip=$.ip==undefined?"127.0.0.1":$.ip;
            var port=$.port==undefined?"1818":$.port;
            var url="ws://"+ip +":"+   port+"/";
            var ws = createWebSocket(url);//连接服务器

            if(ws==null){
                alert("您的浏览器不支持WEBSOCKET协议。 ");
                return ;
            }
            ws.onopen = function(event){
                ws.send("{\"type\":\""+type+"\",\"position\":\""+position+"\",\"operation\":\""+operation+"\",\"count\":\""+count+"\",\"interval\":\""+interval+"\",\"data\":\""+data+"\"}");
            };
            //ws.onclose = function(event){alert("已经与服务器断开连接\r\n当前连接状态："+this.readyState);};
            ws.onmessage=function(e){
                if(typeof(callback)=="function"){
                    callback(e.data,this);
                }
                var relclose;
                //是否关闭
                if(isclose==false){
                    relclose=true;
                }else{
                    relclose=false;
                }

                if(!relclose ){

                    ws.close();
                }
            }
        }
    }

    var currency=function (obj){
        this.type=obj.type;
        this.position=obj.position;
        this.operation=obj.operation;
        this.isclose=obj.isclose;
        this.count=obj.count;
        this.interval=obj.interval;
        this.data=obj.data;
        if( typeof(obj.callback)=="function"){
            this.callback=obj.callback;

        }
        WebSocketClass.call(this );
    }
    var extend=function(o,n,override){
        for(var p in n)if(n.hasOwnProperty(p) && (!o.hasOwnProperty(p) || override))o[p]=n[p];
    };



    // $.Meter.on({operation:"open"},function(w,d){  } )
//仪表
    $.Meter={
        type:"YB",
        open:function(obj){
            extend(obj,{operation:"open",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        close:function(obj){
            extend(obj,{operation:"close",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();

        },
        getdata:function(obj){
            extend(obj,{operation:"getdata",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        ybclear:function(obj){
            extend(obj,{operation:"ybclear",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    $.WPY={
        type:'WPY',

        picture:function(obj){
            extend(obj,{operation:"picture",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    //刷卡器
    $.MWRF35={
        type:'MWRF35',
        open:function(obj){
            $.Meter.open.call(this,obj);
        },
        close:function(obj){
            $.Meter.close.call(this,obj);
        },
        remotesetid:function(obj){
            extend(obj,{operation:"remotesetid",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        remoteclearsetid:function(obj){
            extend(obj,{operation:"remoteclearsetid",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        getid:function(obj){
            extend(obj,{operation:"getid",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        getic:function(obj){
            extend(obj,{operation:"getic",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        setic:function(obj){
            extend(obj,{operation:"setic",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        seticloop:function(obj){
            extend(obj,{operation:"seticloop",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        seticbyte:function(obj){
            extend(obj,{operation:"seticbyte",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        changepassword:function(obj){
            extend(obj,{operation:"changepassword",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        loadkey:function(obj){
            extend(obj,{operation:"loadkey",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    //刷卡器
    $.RFID={
        type:'RFID',
        open:function(obj){
            $.Meter.open.call(this,obj);
        },
        close:function(obj){
            $.Meter.close.call(this,obj);
        },
        getdata:function(obj){
            $.Meter.getdata.call(this,obj);
        }
    }
    //LED
    $.LED={
        type:'LED',
        sendmsg:function(obj){
            extend(obj,{operation:"sendmsg",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    //红外
    $.HWGPIO={
        type:'HWGPIO',
        open:function(obj){
            $.Meter.open.call(this,obj);
        },
        close:function(obj){
            $.Meter.close.call(this,obj);
        },
        gethwdata:function(obj){
            extend(obj,{operation:"gethwdata",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        gethwalldata:function(obj){
            extend(obj,{operation:"gethwalldata",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    $.TTS={
        type:'TTS',
        sendmsg:function(obj){
            $.LED.sendmsg.call(this,obj);
        }
    }
    //道闸
    $.GATEWAY={
        type:'GATEWAY',
        open:function(obj){
            $.Meter.open.call(this,obj);
        },
        close:function(obj){
            $.Meter.close.call(this,obj);
        },
        up:function(obj){
            extend(obj,{operation:"up",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        down:function(obj){
            extend(obj,{operation:"down",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }

    //温湿度
    $.WSD={
        type:'WSD',
        open:function(obj){
            $.Meter.open.call(this,obj);
        },
        close:function(obj){
            $.Meter.close.call(this,obj);
        },
        getdata:function(obj){
            $.Meter.getdata.call(this,obj);
        }
    }
    //红绿灯
    $.RGLight={
        type:'RGLight',
        open:function(obj){
            $.Meter.open.call(this,obj);
        },
        close:function(obj){
            $.Meter.close.call(this,obj);
        },
        setrgl:function(obj){
            obj.operation="setrgl";
            $.LED.sendmsg.call(this,obj);
        } ,
        monitor:function(obj){
            obj.operation="monitor";
            $.LED.sendmsg.call(this,obj);
        }
    }
    //声光报警
    $.VoiceAlarm={
        type:'VoiceAlarm',
        open:function(obj){
            $.Meter.open.call(this,obj);
        },
        close:function(obj){
            $.Meter.close.call(this,obj);
        },
        setstate:function(obj){
            extend(obj,{operation:"setstate",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    //车号识别
    $.CarLicence={
        type:'CarLicence',
        open:function(obj){
            $.Meter.open.call(this,obj);
        },
        close:function(obj){
            $.Meter.close.call(this,obj);
        },
        monitor:function(obj){
            extend(obj,{operation:"monitor",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        } ,
        ManualTtrigger:function(obj){
            extend(obj,{operation:"ManualTtrigger",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
//视频抓拍图片 海康
    $.Video={
        type:'Video',
        sendmsg:function(obj){
            $.LED.sendmsg.call(this,obj);
        }

    }
    $.YTCard={
        type:'YTCard',
        check_card:function(obj){
            extend(obj,{operation:"check_card",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();

        } ,
        Client_ID:function(obj){
            extend(obj,{operation:"Client_ID",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    $.PCL1761={
        type:'PCL1761',
        openred:function(obj){
            extend(obj,{operation:"openred",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();

        } ,
        opengreen:function(obj){
            extend(obj,{operation:"opengreen",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        } ,
        qhs:function(obj){
            extend(obj,{operation:"qhs",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        } ,
        hhs:function(obj){
            extend(obj,{operation:"hhs",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    $.SMS={
        type:'SMS',
        open:function(obj){
            $.Meter.open.call(this,obj);
        } ,
        close:function(obj){
            $.Meter.close.call(this,obj);
        } ,
        sendmsg:function(obj){
            $.LED.sendmsg.call(this,obj);
        }
    }
    $.Route={
        type:'Route',
        get:function(obj){
            extend(obj,{operation:"get",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        } ,
        set:function(obj){
            extend(obj,{operation:"set",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }

    }
    $.Command={
        method:function(obj){
            extend(obj,{operation:"command"});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    $.Runstartexe={
        method:function(obj){
            extend(obj,{operation:"runstartexe"});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    $.Print={
        type:'Print',
        sendmsg:function(obj){
            $.LED.sendmsg.call(this,obj);
        }
    }
    $.VoiceSpeak={
        type:'VoiceSpeak',
        startsession:function(obj){
            extend(obj,{operation:"startsession",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        } ,
        stopsession:function(obj){
            extend(obj,{operation:"stopsession",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    $.HLDBJ={
        type:'Hldbj',
        open:function(obj){
            $.Meter.open.call(this,obj);
        } ,
        close:function(obj){
            $.Meter.close.call(this,obj);
        } ,
        getdata:function(obj){
            $.Meter.getdata.call(this,obj);
        },
        setdata:function(obj){
            extend(obj,{operation:"setdata",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    $.HLPMJ={
        type:'Hlpmj',
        open:function(obj){
            $.Meter.open.call(this,obj);
        } ,
        close:function(obj){
            $.Meter.close.call(this,obj);
        } ,
        setdata:function(obj){
            extend(obj,{operation:"setdata",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        getstr:function(obj){
            extend(obj,{operation:"getstr",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    $.Sfz={
        type:'Sfz',
        getinfo:function(obj){
            extend(obj,{operation:"getinfo",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }

    $.SendRecive={
        type:'sendrecive',
        open:function(obj){
            $.Meter.open.call(this,obj);
        } ,
        close:function(obj){
            $.Meter.close.call(this,obj);
        } ,
        send:function(obj){
            extend(obj,{operation:"send",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        recive:function(obj){
            extend(obj,{operation:"recive",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }

    $.LDCS={
        type:'LDCS',
        open:function(obj){
            $.Meter.open.call(this,obj);
        } ,
        close:function(obj){
            $.Meter.close.call(this,obj);
        } ,
        getdata:function(obj){
            extend(obj,{operation:"getdata",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
    }
    $.Printer={
        type:'Printer',
        open:function(obj){
            $.Meter.open.call(this,obj);
        } ,
        close:function(obj){
            $.Meter.close.call(this,obj);
        } ,
        getstate:function(obj){
            extend(obj,{operation:"getstate",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
    }
    $.Bank={
        type:'BankInfo',
        open:function(obj){
            extend(obj,{operation:"open",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        } ,
        getid:function(obj){
            extend(obj,{operation:"getid",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        } ,
        close:function(obj){
            extend(obj,{operation:"close",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        } ,
        getcardinfo:function(obj){
            extend(obj,{operation:"getcardinfo",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        getAHQC:function(obj){
            extend(obj,{operation:"getAHQC",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
    }
    $.EncryptKeyBoder={
        type:'EncryptKeyBoder',
        loadkey:function (obj){
            extend(obj,{operation:"loadkey",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        loadacc:function (obj){
            extend(obj,{operation:"loadacc",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        loadpwd:function (obj){
            extend(obj,{operation:"loadpwd",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        lispwd:function (obj){
            extend(obj,{operation:"lispwd",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        convermac:function (obj){
            extend(obj,{operation:"convermac",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    $.uploadfile={
        upload:function(obj){
            extend(obj,{operation:"upload",type:'fileupload'});
            var c1= new currency(obj);
            c1.web_senddata();
        }
    }
    $.cardsender={
        type:'cardsender',
        open:function(obj){
            extend(obj,{operation:"open",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        } ,
        close:function(obj){
            extend(obj,{operation:"close",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        } ,
        init:function(obj){
            extend(obj,{operation:"init",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        yidongrfwei:function(obj){
            extend(obj,{operation:"yidongrfwei",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        duka:function(obj){
            extend(obj,{operation:"duka",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        chuka:function(obj){
            extend(obj,{operation:"chuka",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        huishou:function(obj){
            extend(obj,{operation:"huishou",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        state:function(obj){
            extend(obj,{operation:"state",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        jinka:function(obj){
            extend(obj,{operation:"jinka",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        jinzhi:function(obj){
            extend(obj,{operation:"jinzhi",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        xiaoyan:function(obj){
            extend(obj,{operation:"xiaoyan",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        readic:function(obj){
            extend(obj,{operation:"readic",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        },
        writeic:function(obj){
            extend(obj,{operation:"writeic",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }	,
        getAHQC:function(obj){
            extend(obj,{operation:"getAHQC",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }	,
        getcardinfo:function(obj){
            extend(obj,{operation:"getcardinfo",type:this.type});
            var c1= new currency(obj);
            c1.web_senddata();
        }

    }
}(device));