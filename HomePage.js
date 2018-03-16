'use-strict';

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View
} from 'react-native';

export default class HomePage extends Component<{}> {
  static navigationOptions = {
    title: 'addr',
  };
  render() {
    return (
      <View style={styles.container}>
      <Text style={styles.welcome}>
      Welcome to React Native!
      </Text>
      <Text style={styles.instructions}>
      To get started, edit App.js
      </Text>
      <Text style={styles.instructions}>
      Something something{"\n"}More something
      </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
