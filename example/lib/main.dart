import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:open_appstore/open_appstore.dart';


void main() => runApp(MyHomePage());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp();
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  final AndroidController = TextEditingController();
  final iOSController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: new Scaffold(
            body: new Center(
                child: Column(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      new Container(
                          width: 200.0,
                          child: TextField(
                            controller: AndroidController,
                            decoration: InputDecoration(
                                border: InputBorder.none,
                                hintText: 'AndroidPackageName'
                            ),
                          )
                      ),
                      new Container(
                          width: 200.0,
                          child: TextField(
                            controller: iOSController,
                            decoration: InputDecoration(
                                border: InputBorder.none,
                                hintText: 'iOSPackageName'
                            ),
                          )
                      ),
                      RaisedButton(
                          child: Text('Move to AppStore'),
                          onPressed: () => OpenAppstore.launch(androidAppId: AndroidController.text, iOSAppId: iOSController.text)
                      )
                    ]
                )
            )
        ));
  }
}