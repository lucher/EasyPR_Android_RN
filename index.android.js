import React, {Component} from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  NativeModules,
  ToastAndroid,
  TouchableNativeFeedback,
} from 'react-native';

import RCTDeviceEventEmitter from 'RCTDeviceEventEmitter';  
 var temp = '这里是结果显示区域';
class helloworld extends Component {

 
  constructor(props) {
    super(props);
    this.state = {showText:temp};
     setInterval(() => {
      this.setState({ showText: temp });
    }, 1000);
  }
  render() {
    return (
      <View style={styles.container}>
       <TouchableNativeFeedback
        onPress={this.onClick}
        background={TouchableNativeFeedback.SelectableBackground()}>
      <View style={{width: 350, height: 150, backgroundColor: 'gray'}}>
        <Text style={{margin: 10, color:'white',fontSize:28}}>点我点我 这个页面来自ReactNative</Text>
        <Text style={{margin: 5, color:'blue',fontSize:25}}>{this.state.showText}</Text>
      </View>
      
    </TouchableNativeFeedback>
      </View>
    );
  }

componentDidMount(){
    RCTDeviceEventEmitter.addListener('notify',function(msg){
        // helloworld.setState({showText:'组件被刷新了'});
      temp =  msg.content
        ToastAndroid.show("收到来自原生页面的消息:" + "\n" + msg.content, ToastAndroid.SHORT)
    })
}

 
  /**
   * 调用原生页面
   * ControlPCActivity是你想跳转到的页面
   */
  onClick() {
    NativeModules.MyMapIntentModule.startActivityByClassname('com.easypr.rn.CameraActivity')  
  }
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
 
});


 
AppRegistry.registerComponent('helloworld', () => helloworld);
