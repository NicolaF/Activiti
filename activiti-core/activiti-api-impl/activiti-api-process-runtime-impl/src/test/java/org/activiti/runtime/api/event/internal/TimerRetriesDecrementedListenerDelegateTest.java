/*
 * Copyright 2019 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.runtime.api.event.internal;

import org.activiti.api.process.model.events.BPMNTimerRetriesDecrementedEvent;
import org.activiti.api.process.runtime.events.listener.BPMNElementEventListener;
import org.activiti.api.runtime.event.impl.BPMNTimerRetriesDecrementedEventImpl;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.runtime.api.event.impl.ToTimerRetriesDecrementedConverter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TimerRetriesDecrementedListenerDelegateTest {

    private TimerRetriesDecrementedListenerDelegate listenerDelegate;

    @Mock
    private BPMNElementEventListener<BPMNTimerRetriesDecrementedEvent> listener;

    @Mock
    private ToTimerRetriesDecrementedConverter converter;

    @Before
    public void setUp() {
        initMocks(this);
        listenerDelegate = new TimerRetriesDecrementedListenerDelegate(Collections.singletonList(listener), converter);
    }

    @Test
    public void shouldCallRegisteredListenersWhenConvertedEventIsNotEmpty() {
        //given
        ActivitiEntityEvent internalEvent = mock(ActivitiEntityEvent.class);
        BPMNTimerRetriesDecrementedEventImpl convertedEvent = new BPMNTimerRetriesDecrementedEventImpl();
        given(converter.from(internalEvent)).willReturn(Optional.of(convertedEvent));

        //when
        listenerDelegate.onEvent(internalEvent);

        //then
        verify(listener).onEvent(convertedEvent);
    }

    @Test
    public void shouldDoNothingWhenConvertedEventIsEmpty() {
        //given
        ActivitiEntityEvent internalEvent = mock(ActivitiEntityEvent.class);
        given(converter.from(internalEvent)).willReturn(Optional.empty());

        //when
        listenerDelegate.onEvent(internalEvent);

        //then
        verify(listener, never()).onEvent(any());
    }
}
