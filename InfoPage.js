'use-strict';

import React, { Component } from 'react';
import { Text, View, Button } from 'react-native';

export default class InfoPage extends Component<{}> {
  render() {
    return (
      <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center', backgroundColor: '#93979e' }}>
        <Text style={{fontSize: 20, textAlign: 'center', color: 'white'}}>Developed by Nick Dimitrov and Uneeb Agha. We are based in Seattle!</Text>
        <View style={{height:100}}/>
        <Button
          onPress={() => this.props.navigation.goBack()}
          title="Dismiss"
        />
      </View>
    );
  }
}
