'use-strict';

import React, { Component } from 'react';
import {
  View,
  Text
} from 'react-native';

export default class ReceivePage extends Component<{}> {
  static navigationOptions = {
    title: 'Receive coins',
  };

  render() {
    const { params } = this.props.navigation.state;
    const address = params ? params.address : '';
    return (
      <View>
        <Text>{address}</Text>
      </View>
    );
  }
}
